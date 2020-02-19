package org.hurricanegames.packetholograms.commands;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandResponseException;

public class PacketHologramsHologramListCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramListCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	public void handleCommand() {
		throw new CommandResponseException(ChatColor.GREEN + "Holograms: " + String.join(", ", helper.getHolograms().getHologramsNames()));
	}

	@Override
	protected String getHelpExplainMessage() {
		return "lists holograms";
	}

}
