package org.hurricanegames.packetholograms.commands;

import org.hurricanegames.packetholograms.PacketHologramsPlugin;
import org.hurricanegames.packetholograms.holograms.HologramController;
import org.hurricanegames.pluginlib.commands.CommandHelper;
import org.hurricanegames.pluginlib.commands.CommandsLocalization;
import org.hurricanegames.pluginlib.configurations.builtin.DefaultCommandsLocalization;
import org.hurricanegames.pluginlib.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.pluginlib.playerinfo.BukkitPlayerInfoProvider;

public class PacketHologramsCommandHelper extends CommandHelper<PacketHologramsPlugin, CommandsLocalization, BukkitPlayerInfo, BukkitPlayerInfoProvider> {

	public PacketHologramsCommandHelper(PacketHologramsPlugin plugin) {
		super(plugin, DefaultCommandsLocalization.IMMUTABLE, BukkitPlayerInfoProvider.INSTANCE);
	}

	public HologramController getHolograms() {
		return getPlugin().getController();
	}

}
