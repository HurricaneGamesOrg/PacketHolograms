package org.hurricanegames.packetholograms.commands;

import java.io.File;

import org.hurricanegames.commandlib.commands.CommandHelper;
import org.hurricanegames.commandlib.commands.CommandMessages;
import org.hurricanegames.commandlib.providers.messages.DefaultMessages;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfoProvider;
import org.hurricanegames.packetholograms.holograms.HologramController;

public class PacketHologramsCommandHelper extends CommandHelper<CommandMessages, BukkitPlayerInfo, BukkitPlayerInfoProvider> {

	protected final HologramController holograms;

	public PacketHologramsCommandHelper(HologramController holograms) {
		super(
			new DefaultMessages() {
				@Override
				protected File getStorageFile() {
					return null;
				}
			},
			new BukkitPlayerInfoProvider()
		);
		this.holograms = holograms;
	}

	public HologramController getHolograms() {
		return holograms;
	}

}
