package org.hurricanegames.packetholograms.holograms;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.IntStream;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import org.hurricanegames.packetholograms.EntityIdGenerator;
import org.hurricanegames.packetholograms.PacketHologramsPlugin;
import org.hurricanegames.packetholograms.integrations.PlaceholderAPIIntergration;
import org.hurricanegames.packetholograms.utils.MiscUtils;
import org.hurricanegames.packetholograms.utils.MutableBoolean;
import org.hurricanegames.pluginlib.configurations.ConfigurationUtils;
import org.hurricanegames.pluginlib.utils.bukkit.types.world.Coord2D;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import protocolsupport.api.Connection;
import protocolsupport.api.Connection.PacketListener;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.events.ConnectionOpenEvent;

public class HologramController {

	protected static final String METADATA_KEY_PACKET_LISTENER = "PacketHolograms_PacketListener";
	protected static final String METADATA_KEY_CURRENT_WORLD = "PacketHolograms_CurrentWorld";
	protected static final String METADATA_KEY_VISIBLE_HOLOGRAMS = "PacketHolograms_VisibleHolograms";

	protected final PacketHologramsPlugin plugin;
	protected final PacketUtil packetutil;

	protected final File storageFile;

	protected final PlayerWorldTracker worldTracker = new PlayerWorldTracker(METADATA_KEY_CURRENT_WORLD);
	protected final PlayerViewHologramsHandlerAttachListener viewHologramsAttachListener = new PlayerViewHologramsHandlerAttachListener();

	public HologramController(PacketHologramsPlugin plugin) {
		this.plugin = plugin;
		this.storageFile = new File(plugin.getDataFolder(), "storage.yml");
		this.packetutil = new PacketUtil();
	}

	protected final Map<String, Hologram> hologramByName = new HashMap<>();
	protected final Map<String, Map<Coord2D, Set<Hologram>>> hologramsByLocation = new ConcurrentHashMap<>();

	public Set<String> getHologramsNames() {
		return Collections.unmodifiableSet(hologramByName.keySet());
	}

	public Collection<Hologram> getHolograms() {
		return Collections.unmodifiableCollection(hologramByName.values());
	}

	public Hologram getHologram(String name) {
		return hologramByName.get(name);
	}

	public void addHologram(Hologram hologram) {
		String name = hologram.getName();
		if ((name != null) && hologramByName.containsKey(name)) {
			throw new IllegalArgumentException("Hologram " + name + " already exists");
		}

		addHologram0(hologram);

		Coord2D chunkCoord = Coord2D.createChunkCoord(hologram.getLocation());
		for (Connection connection : ProtocolSupportAPI.getConnections()) {
			String world = connection.getMetadata(METADATA_KEY_CURRENT_WORLD);
			if ((world == null) || !world.equals(hologram.getWorld())) {
				return;
			}

			Map<Hologram, int[]> chunkHolograms = getPlayerChunkHologramMap(connection, chunkCoord);
			if (chunkHolograms == null) {
				return;
			}

			List<Object> packets = new ArrayList<>();
			spawnHologram(chunkHolograms, packets, hologram, MiscUtils.getConnectionPlayer(connection));
			packets.forEach(connection::sendPacket);
		}

		save();
	}

	protected void addHologram0(Hologram hologram) {
		String name = hologram.getName();
		if (name != null) {
			hologramByName.put(name, hologram);
		}
		hologramsByLocation
		.computeIfAbsent(hologram.getWorld(), k -> new ConcurrentHashMap<>())
		.computeIfAbsent(Coord2D.createChunkCoord(hologram.getLocation()), k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
		.add(hologram);
	}

	public void deleteHologram(Hologram hologram) {
		Coord2D chunkCoord = Coord2D.createChunkCoord(hologram.getLocation());

		String name = hologram.getName();
		if (name != null) {
			hologramByName.remove(name);
		}
		hologramsByLocation
		.getOrDefault(hologram.getWorld(), Collections.emptyMap())
		.getOrDefault(chunkCoord, Collections.emptySet())
		.remove(hologram);

		for (Connection connection : ProtocolSupportAPI.getConnections()) {
			Map<Hologram, int[]> chunkHolograms = getPlayerChunkHologramMap(connection, chunkCoord);
			if (chunkHolograms == null) {
				return;
			}
			int[] entityIds = chunkHolograms.remove(hologram);
			if (entityIds != null) {
				for (int entityId : entityIds) {
					connection.sendPacket(packetutil.createEntityDestroyPacket(entityId));
				}
			}
		}

		save();
	}

	public Hologram setHologramLocation(Hologram hologram, String world, Vector location) {
		deleteHologram(hologram);

		Hologram newHologram = new Hologram(hologram.getName(), world, location, hologram.getLines());
		addHologram(newHologram);

		save();

		return newHologram;
	}

	public Hologram setHologramLines(Hologram hologram, List<String> lines) {
		deleteHologram(hologram);

		Hologram newHologram = new Hologram(hologram.getName(), hologram.getWorld(), hologram.getLocation(), lines);
		addHologram(newHologram);

		save();

		return newHologram;
	}

	protected static Map<Hologram, int[]> getPlayerChunkHologramMap(Connection connection, Coord2D chunkcoord) {
		Map<Coord2D, Map<Hologram, int[]>> visible = connection.getMetadata(METADATA_KEY_VISIBLE_HOLOGRAMS);
		if (visible == null) {
			return null;
		}
		return visible.get(chunkcoord);
	}

	protected void spawnHologram(Map<Hologram, int[]> chunkHolograms, List<Object> packets, Hologram hologram, OfflinePlayer player) {
		List<String> lines = hologram.getLines();
		int[] entityIds = new int[lines.size()];
		Vector location = hologram.getLocation().clone();
		for (int i = 0; i < lines.size(); i++) {
			int entityId = EntityIdGenerator.INSTANCE.nextId();
			entityIds[i] = entityId;
			packets.add(packetutil.createEntitySpawnPacket(PacketUtil.ARMORSTAND_LIVING_TYPE_ID, MiscUtils.fastRandomUUID(), entityId, location));
			packets.add(packetutil.createEntityMetadataPacket(entityId, Arrays.asList(
				PacketUtil.createWatchableObject(
					PacketUtil.DW_BASE_FLAGS_INDEX,
					PacketUtil.DW_BYTE_SERIALIZER,
					Byte.valueOf((byte) PacketUtil.DW_BASE_FLAGS_INVISIBLE_OFFSET)
				),
				PacketUtil.createWatchableObject(
					PacketUtil.DW_BASE_NAME_VISIBLE_INDEX,
					PacketUtil.DW_BOOLEAN_SERIALIZER,
					Boolean.TRUE
				),
				PacketUtil.createWatchableObject(
					PacketUtil.DW_ARMORSTANDDATA_INDEX,
					PacketUtil.DW_BYTE_SERIALIZER,
					Byte.valueOf((byte) PacketUtil.DW_ARMORSTANDDATA_MARKER_OFFSET)
				),
				PacketUtil.createWatchableObject(
					PacketUtil.DW_BASE_NAME_INDEX,
					PacketUtil.DW_OPTIONAL_CHAT_SERIALIZER,
					Optional.of(WrappedChatComponent.fromText(PlaceholderAPIIntergration.setBracketPlaceholders(player, lines.get(i))).getHandle())
				)
			)));
			location.setY(location.getY() - Hologram.LINE_H);
		}
		chunkHolograms.put(hologram, entityIds);
	}

	protected class PlayerViewHologramsHandlerAttachListener implements Listener {

		@EventHandler
		protected void onConnectionOpen(ConnectionOpenEvent event) {
			Connection connection = event.getConnection();
			PlayerViewHologramsHandler handler = new PlayerViewHologramsHandler(connection);
			connection.addMetadata(METADATA_KEY_PACKET_LISTENER, handler);
			connection.addPacketListener(handler);
		}

	}

	protected class PlayerViewHologramsHandler extends PacketListener {

		protected final Connection connection;
		public PlayerViewHologramsHandler(Connection connection) {
			this.connection = connection;
		}

		@Override
		public void onPacketSending(PacketEvent event) {
			String world = connection.getMetadata(METADATA_KEY_CURRENT_WORLD);
			if (world == null) {
				return;
			}

			PacketContainer packet = PacketContainer.fromPacket(event.getPacket());
			if (packet.getType() == PacketType.Play.Server.RESPAWN) {
				handleRespawn();
			} else if (packet.getType() == PacketType.Play.Server.UNLOAD_CHUNK) {
				handleChunkUnload(event, packet);
			} else if (packet.getType() == PacketType.Play.Server.MAP_CHUNK) {
				handleChunkData(event, packet, world);
			}
		}

		protected void handleRespawn() {
			connection.removeMetadata(METADATA_KEY_VISIBLE_HOLOGRAMS);
		}

		protected void handleChunkUnload(PacketEvent event, PacketContainer packet) {
			Map<Coord2D, Map<Hologram, int[]>> visible = connection.getMetadata(METADATA_KEY_VISIBLE_HOLOGRAMS);
			if (visible == null) {
				return;
			}

			StructureModifier<Integer> integers = packet.getIntegers();
			Map<Hologram, int[]> chunkHolograms = visible.remove(new Coord2D(integers.read(0), integers.read(1)));
			if (chunkHolograms == null) {
				return;
			}

			List<Object> packets = event.getPackets();
			for (int[] entityIds : chunkHolograms.values()) {
				for (int entityId : entityIds) {
					packets.add(packetutil.createEntityDestroyPacket(entityId));
				}
			}
		}

		protected void handleChunkData(PacketEvent event, PacketContainer packet, String world) {
			Map<Coord2D, Map<Hologram, int[]>> visible = connection.computeMetadata(METADATA_KEY_VISIBLE_HOLOGRAMS, (k, v) -> v != null ? v : new HashMap<>());
			MutableBoolean wasntLoaded = new MutableBoolean();
			StructureModifier<Integer> integers = packet.getIntegers();
			Coord2D chunkCoord = new Coord2D(integers.read(0), integers.read(1));
			Map<Hologram, int[]> chunkHolograms = visible.compute(chunkCoord, (k, v) -> {
				if (v != null) {
					return v;
				}
				wasntLoaded.set(true);
				return new HashMap<>();
			});
			if (!wasntLoaded.get()) {
				return;
			}

			OfflinePlayer player = MiscUtils.getConnectionPlayer(connection);
			List<Object> packets = event.getPackets();
			hologramsByLocation
			.getOrDefault(world, Collections.emptyMap())
			.getOrDefault(chunkCoord, Collections.emptySet())
			.forEach(hologram -> spawnHologram(chunkHolograms, packets, hologram, player));
		}

	}


	protected static final String config_holograms_path = "holograms";

	private boolean loaded = false;
	public void initAndLoad() {
		if (loaded) {
			return;
		}

		ConfigurationSection configuration = YamlConfiguration.loadConfiguration(storageFile);
		{
			ConfigurationSection hologramsSection = configuration.getConfigurationSection(config_holograms_path);
			if (hologramsSection != null) {
				for (String hologramName : hologramsSection.getKeys(false)) {
					addHologram0(Hologram.fromConfigurationSection(hologramName, hologramsSection.getConfigurationSection(hologramName)));
				}
			}
		}

		PluginManager pluginManager = plugin.getServer().getPluginManager();
		pluginManager.registerEvents(worldTracker, plugin);
		pluginManager.registerEvents(viewHologramsAttachListener, plugin);

		loaded = true;
	}

	public void save() {
		YamlConfiguration configuration = new YamlConfiguration();
		{
			ConfigurationSection hologramsSection = configuration.createSection(config_holograms_path);
			for (Map.Entry<String, Hologram> entry : hologramByName.entrySet()) {
				hologramsSection.set(entry.getKey(), entry.getValue().toConfigurationSection());
			}
		}
		try {
			ConfigurationUtils.safeSave(configuration, storageFile);
		} catch (UncheckedIOException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to save hologram controller storage", e);
		}
	}

	public void destroy() {
		HandlerList.unregisterAll(worldTracker);
		HandlerList.unregisterAll(viewHologramsAttachListener);
		for (Connection connection : ProtocolSupportAPI.getConnections()) {
			connection.removePacketListener(connection.removeMetadata(METADATA_KEY_PACKET_LISTENER));
			connection.removeMetadata(METADATA_KEY_CURRENT_WORLD);
			connection.<Map<Coord2D, Map<Hologram, int[]>>>removeMetadata(METADATA_KEY_VISIBLE_HOLOGRAMS).values().stream()
			.map(Map::values)
			.flatMap(Collection::stream)
			.flatMapToInt(IntStream::of)
			.forEach(entityId -> connection.sendPacket(packetutil.createEntityDestroyPacket(entityId)));
		}
	}


}
