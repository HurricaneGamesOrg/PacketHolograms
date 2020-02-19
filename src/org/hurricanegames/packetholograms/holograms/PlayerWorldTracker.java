package org.hurricanegames.packetholograms.holograms;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;

public class PlayerWorldTracker implements Listener {

	protected final String metadataKey;
	public PlayerWorldTracker(String metadataKey) {
		this.metadataKey = metadataKey;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	protected void onJoin(PlayerJoinEvent event) {
		trySetCurrentWorld(event.getPlayer(), event.getPlayer().getWorld().getName());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	protected void onTeleport(PlayerTeleportEvent event) {
		trySetCurrentWorld(event.getPlayer(), event.getTo().getWorld().getName());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	protected void onRespawn(PlayerRespawnEvent event) {
		trySetCurrentWorld(event.getPlayer(), event.getRespawnLocation().getWorld().getName());
	}

	protected void trySetCurrentWorld(Player player, String world) {
		Connection connection = ProtocolSupportAPI.getConnection(player);
		if (connection != null) {
			connection.addMetadata(metadataKey, world);
		}
	}

}