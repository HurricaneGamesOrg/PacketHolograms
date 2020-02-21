package org.hurricanegames.packetholograms.commands;

import org.hurricanegames.commandlib.commands.CommandRouter;
import org.hurricanegames.packetholograms.commands.line.PacketHologramsHologramLineCommands;

public class PacketHologramsCommands extends CommandRouter<PacketHologramsCommandHelper> {

	public PacketHologramsCommands(PacketHologramsCommandHelper helper) {
		super(helper);
		addCommand("list", new PacketHologramsHologramListCommand(helper));
		addCommand("add", new PacketHologramsHologramAddCommand(helper));
		addCommand("remove", new PacketHologramsHologramRemoveCommand(helper));
		addCommand("move", new PacketHologramsHologramMoveCommand(helper));
		addCommand("line", new PacketHologramsHologramLineCommands(helper));
	}

}
