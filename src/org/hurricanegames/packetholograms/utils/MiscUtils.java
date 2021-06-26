package org.hurricanegames.packetholograms.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.hurricanegames.pluginlib.utils.bukkit.types.UnknownOfflinePlayer;

import protocolsupport.api.Connection;
import protocolsupport.api.utils.Profile;

public class MiscUtils {

	public static UUID fastRandomUUID() {
		Random random = ThreadLocalRandom.current();
		return new UUID(random.nextLong(), random.nextLong());
	}

	public static OfflinePlayer getConnectionPlayer(Connection connection) {
		Player player = connection.getPlayer();
		if (player != null) {
			return player;
		}
		Profile profile = connection.getProfile();
		return new UnknownOfflinePlayer(profile.getUUID(), profile.getName());
	}

}
