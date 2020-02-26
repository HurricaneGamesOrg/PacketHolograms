package org.hurricanegames.packetholograms.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramMoveCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramMoveCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentSenderPlayer.class) Player player,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Hologram hologram
	) {
		Location location = player.getLocation();
		helper.getHolograms().moveHologram(hologram, location.getWorld().getName(), location.toVector());

		throw new CommandResponseException("Hologram {0} moved", hologram.getName());
	}


	@Override
	protected String getHelpExplainMessage() {
		return "moves hologram";
	}

}
