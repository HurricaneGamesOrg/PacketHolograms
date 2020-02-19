package org.hurricanegames.packetholograms;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.commandlib.commands.BukkitCommandExecutor;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommands;
import org.hurricanegames.packetholograms.holograms.HologramController;
import org.hurricanegames.packetholograms.integrations.PlaceholderAPIIntergration;

import com.comphenix.protocol.ProtocolLibrary;

public class PacketHologramsPlugin extends JavaPlugin {

	private HologramController controller;

	@Override
	public void onEnable() {
		PlaceholderAPIIntergration.init();
		controller = new HologramController(this, new EntityIdAllocator(), ProtocolLibrary.getProtocolManager());
		controller.initAndLoad();
		getCommand("holograms").setExecutor(new BukkitCommandExecutor(new PacketHologramsCommands(new PacketHologramsCommandHelper(controller)), "packetholograms.admin"));
	}

	@Override
	public void onDisable() {
		controller.save();
		controller.destroy();
	}

}
