package org.hurricanegames.packetholograms.holograms;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import protocolsupport.api.MaterialAPI;

public class PacketUtil {

	protected final ProtocolManager protocollib;
	protected PacketUtil(ProtocolManager protocollib) {
		this.protocollib = protocollib;
	}

	protected static final int ARMORSTAND_LIVING_TYPE_ID = MaterialAPI.getEntityLivingTypeNetworkId(EntityType.ARMOR_STAND);
	protected static final Serializer DW_BYTE_SERIALIZER = Registry.get(Byte.class, false);
	protected static final Serializer DW_OPTIONAL_CHAT_SERIALIZER = Registry.getChatComponentSerializer(true);
	protected static final Serializer DW_BOOLEAN_SERIALIZER = Registry.get(Boolean.class, false);
	protected static final int DW_BASE_FLAGS_INDEX = 0;
	protected static final int DW_BASE_FLAGS_INVISIBLE_OFFSET = 0x20;
	protected static final int DW_BASE_NAME_INDEX = 2;
	protected static final int DW_BASE_NAME_VISIBLE_INDEX = 3;
	protected static final int DW_ARMORSTANDDATA_INDEX = 14;
	protected static final int DW_ARMORSTANDDATA_MARKER_OFFSET = 0x10;

	protected Object createEntitySpawnPacket(int type, UUID uuid, int entityId, Vector location) {
		PacketContainer packet = protocollib.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
		packet.getUUIDs().write(0, uuid);
		packet.getIntegers()
		.write(0, entityId)
		.write(1, type);
		packet.getDoubles()
		.write(0, location.getX())
		.write(1, location.getY())
		.write(2, location.getZ());
		return packet.getHandle();
	}

	protected Object createEntityDestroyPacket(int... entityIds) {
		PacketContainer packet = protocollib.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		packet.getIntegerArrays().write(0, entityIds);
		return packet.getHandle();
	}

	protected Object createEntityMetadataPacket(int entityId, List<WrappedWatchableObject> objects) {
		PacketContainer metadata = protocollib.createPacket(PacketType.Play.Server.ENTITY_METADATA);
		metadata.getIntegers().write(0, entityId);
		metadata.getWatchableCollectionModifier().write(0, objects);
		return metadata.getHandle();
	}

	protected static WrappedWatchableObject createWatchableObject(int index, Serializer serializer, Object value) {
		return new WrappedWatchableObject(new WrappedDataWatcherObject(index, serializer), value);
	}

}
