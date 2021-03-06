package org.hurricanegames.packetholograms.commands;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.hurricanegames.packetholograms.holograms.Hologram;
import org.hurricanegames.pluginlib.commands.CommandBasic;
import org.hurricanegames.pluginlib.utils.bukkit.MiscBukkitUtils;

public abstract class PacketHologramsCommandBasic extends CommandBasic<PacketHologramsCommandHelper> {

	protected PacketHologramsCommandBasic(PacketHologramsCommandHelper helper) {
		super(helper);
	}

	@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class)
	protected class CommandArgumentHologram extends CommandArgumentPositional<Hologram> {

		@Override
		protected Hologram parseValue(String arg) {
			return helper.validateNotNull(helper.getHolograms().getHologram(arg), "Hologram {0} doesn't exist", arg);
		}

		@Override
		protected List<String> complete(String arg) {
			return
				helper.getHolograms().getHolograms().stream()
				.map(Hologram::getName)
				.filter(name -> (name != null) && name.startsWith(arg))
				.collect(Collectors.toList());
		}

		@Override
		protected String getHelpMessage() {
			return "{name}";
		}

	}

	protected class CommandArgumentHologramLineIndex extends CommandArgumentInteger {

		protected final Supplier<Hologram> hologramSupplier;

		protected CommandArgumentHologramLineIndex(
			@CommandArgumentDefinition(PacketHologramsCommandBasic.CommandArgumentHologram.class) Supplier<Hologram> hologramSupplier
		) {
			this.hologramSupplier = hologramSupplier;
		}

		@Override
		protected void validate(int value) {
			int maxIndex = hologramSupplier.get().getLines().size() - 1;
			helper.validateIsTrue(
				(value >= 0) && (value <= maxIndex),
				"Invalid line index {0}, should be between {1} and {2}",
				value, 0, maxIndex
			);
		}

		@Override
		protected String getHelpMessage() {
			return "{line index}";
		}

	}

	protected class CommandArgumentHologramLineText extends CommandArgumentPositional<String> {

		@Override
		protected String parseValue(String arg) {
			return MiscBukkitUtils.colorize(arg);
		}

		@Override
		protected List<String> complete(String arg) {
			return Collections.emptyList();
		}

		@Override
		protected String getHelpMessage() {
			return "{line text}";
		}

	}

}
