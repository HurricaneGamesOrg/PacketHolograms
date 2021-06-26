package org.hurricanegames.packetholograms.commands;

import org.hurricanegames.packetholograms.commands.line.PacketHologramsHologramLineCommands;
import org.hurricanegames.pluginlib.commands.CommandRouter;

public class PacketHologramsCommands extends CommandRouter<PacketHologramsCommandHelper> {

	public PacketHologramsCommands(PacketHologramsCommandHelper helper) {
		super(helper);
		setPermission("packetholograms.admin");
		addCommand("list", new PacketHologramsHologramListCommand(helper));
		addCommand("add", new PacketHologramsHologramAddCommand(helper));
		addCommand("remove", new PacketHologramsHologramRemoveCommand(helper));
		addCommand("move", new PacketHologramsHologramMoveCommand(helper));
		addCommand("line", new PacketHologramsHologramLineCommands(helper));
	}

}
