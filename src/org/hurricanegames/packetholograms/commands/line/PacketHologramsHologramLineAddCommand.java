package org.hurricanegames.packetholograms.commands.line;

import java.util.ArrayList;
import java.util.List;

import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandBasic;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramLineAddCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramLineAddCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandArgumentHologram.class) Hologram hologram,
		@CommandArgumentDefinition(CommandArgumentHologramLineText.class) String lineText
	) {
		List<String> lines = new ArrayList<>(hologram.getLines());
		lines.add(lineText);
		helper.getHolograms().modifyLinesHologram(hologram, lines);

		throw new CommandResponseException("Hologram {0} line {1} added", hologram.getName(), lineText);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "adds line to hologram";
	}

}
