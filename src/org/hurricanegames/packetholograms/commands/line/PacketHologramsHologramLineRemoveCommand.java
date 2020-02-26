package org.hurricanegames.packetholograms.commands.line;

import java.util.ArrayList;
import java.util.List;

import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandBasic;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramLineRemoveCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramLineRemoveCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Hologram hologram,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologramLineIndex.class) Integer lineIndex
	) {
		List<String> lines = new ArrayList<>(hologram.getLines());
		String removedLine = lines.remove(lineIndex.intValue());

		helper.getHolograms().modifyLinesHologram(hologram, lines);

		throw new CommandResponseException("Hologram {0} line {1} removed", hologram.getName(), removedLine);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "removes line from hologram";
	}

}
