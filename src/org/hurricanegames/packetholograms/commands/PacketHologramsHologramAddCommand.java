package org.hurricanegames.packetholograms.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramAddCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramAddCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	protected class CommandArgumentHologramNameNotExisting extends CommandArgumentPositional<String> {

		@Override
		protected String parseValue(String arg) {
			helper.validateIsTrue(helper.getHolograms().getHologram(arg) == null, "Hologram with name {0} already exists", arg);
			return arg;
		}

		@Override
		protected List<String> complete(String arg) {
			return Collections.emptyList();
		}

		@Override
		protected String getHelpMessage() {
			return "{name}";
		}

	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandArgumentSenderPlayer.class) Player player,
		@CommandArgumentDefinition(CommandArgumentHologramNameNotExisting.class) String name,
		@CommandArgumentDefinition(CommandArgumentHologramLineText.class) String line
	) {
		Location location = player.getLocation();
		helper.getHolograms().addHologram(new Hologram(name, location.getWorld().getName(), location.toVector(), Collections.singletonList(line)));

		throw new CommandResponseException(ChatColor.GREEN + "Hologram {0} added", name);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "adds hologram";
	}

}
