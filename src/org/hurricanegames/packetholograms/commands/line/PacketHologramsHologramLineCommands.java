package org.hurricanegames.packetholograms.commands.line;

import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.pluginlib.commands.CommandRouter;

public class PacketHologramsHologramLineCommands extends CommandRouter<PacketHologramsCommandHelper> {

	public PacketHologramsHologramLineCommands(PacketHologramsCommandHelper helper) {
		super(helper);
		addCommand("add", new PacketHologramsHologramLineAddCommand(helper));
		addCommand("insert", new PacketHologramsHologramLineInsertCommand(helper));
		addCommand("set", new PacketHologramsHologramLineSetCommand(helper));
		addCommand("remove", new PacketHologramsHologramLineRemoveCommand(helper));
	}

}
