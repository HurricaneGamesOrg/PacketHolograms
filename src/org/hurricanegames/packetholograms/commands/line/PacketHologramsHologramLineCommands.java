package org.hurricanegames.packetholograms.commands.line;

import org.hurricanegames.commandlib.commands.CommandRouter;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;

public class PacketHologramsHologramLineCommands extends CommandRouter<PacketHologramsCommandHelper> {

	public PacketHologramsHologramLineCommands(PacketHologramsCommandHelper helper) {
		super(helper);
		addCommand("add", new PacketHologramsHologramLineAddCommand(helper));
		addCommand("insert", new PacketHologramsHologramLineInsertCommand(helper));
		addCommand("set", new PacketHologramsHologramLineSetCommand(helper));
		addCommand("remove", new PacketHologramsHologramLineRemoveCommand(helper));
	}

}
