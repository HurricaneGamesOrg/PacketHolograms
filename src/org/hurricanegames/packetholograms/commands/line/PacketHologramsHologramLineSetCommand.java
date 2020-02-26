package org.hurricanegames.packetholograms.commands.line;

import java.util.ArrayList;
import java.util.List;

import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandBasic;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramLineSetCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramLineSetCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Hologram hologram,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologramLineIndex.class) Integer lineIndex,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologramLineText.class) String lineText
	) {
		List<String> lines = new ArrayList<>(hologram.getLines());
		String oldLine = lines.set(lineIndex.intValue(), lineText);
		helper.getHolograms().modifyLinesHologram(hologram, lines);

		throw new CommandResponseException("Hologram {0} line {1} changed to {2}", hologram.getName(), oldLine, lineText);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "sets line in hologram";
	}

}
