package org.hurricanegames.packetholograms.commands.line;

import java.util.ArrayList;
import java.util.List;

import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandBasic;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.holograms.Hologram;

public class PacketHologramsHologramLineInsertCommand extends PacketHologramsCommandBasic {

	public PacketHologramsHologramLineInsertCommand(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Hologram hologram,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologramLineIndex.class) Integer lineIndex,
		@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologramLineText.class) String lineText
	) {
		List<String> lines = new ArrayList<>(hologram.getLines());
		lines.add(lineIndex.intValue(), lineText);

		helper.getHolograms().setHologramLines(hologram, lines);

		throw new CommandResponseException("Hologram {0} line {1} inserted", hologram.getName(), lineText);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "inserts line in hologram";
	}

}
