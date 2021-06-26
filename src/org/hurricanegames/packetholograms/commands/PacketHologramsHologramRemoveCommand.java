package org.hurricanegames.packetholograms.commands;

import org.bukkit.ChatColor;
import org.hurricanegames.packetholograms.holograms.Hologram;
import org.hurricanegames.pluginlib.commands.CommandResponseException;

public class PacketHologramsHologramRemoveCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramRemoveCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Hologram hologram
	) {
		helper.getHolograms().deleteHologram(hologram);

		throw new CommandResponseException(ChatColor.GREEN + "Hologram {0} removed", hologram.getName());
	}

	@Override
	protected String getHelpExplainMessage() {
		return "removes hologram";
	}

}
