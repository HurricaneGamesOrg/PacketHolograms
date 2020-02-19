package org.hurricanegames.packetholograms.utils;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UUIDNameOfflinePlayer implements OfflinePlayer {

	protected final UUID uuid;
	protected final String name;
	public UUIDNameOfflinePlayer(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public UUID getUniqueId() {
		return uuid;
	}
	@Override
	public long getLastPlayed() {
		return System.currentTimeMillis();
	}
	@Override
	public long getLastLogin() {
		return System.currentTimeMillis();
	}
	@Override
	public long getLastSeen() {
		return System.currentTimeMillis();
	}

	@Override
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	@Override
	public boolean isOnline() {
		return getPlayer() != null;
	}

	@Override
	public boolean isOp() {
		return false;
	}
	@Override
	public void setOp(boolean p0) {
	}
	@Override
	public Map<String, Object> serialize() {
		return null;
	}
	@Override
	public Location getBedSpawnLocation() {
		return null;
	}
	@Override
	public long getFirstPlayed() {
		return 0;
	}
	@Override
	public boolean hasPlayedBefore() {
		return true;
	}
	@Override
	public boolean isBanned() {
		return false;
	}
	@Override
	public boolean isWhitelisted() {
		return false;
	}
	@Override
	public void setWhitelisted(boolean p0) {
	}

}