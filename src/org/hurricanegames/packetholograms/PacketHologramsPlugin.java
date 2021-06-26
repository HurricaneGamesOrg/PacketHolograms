package org.hurricanegames.packetholograms;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommandHelper;
import org.hurricanegames.packetholograms.commands.PacketHologramsCommands;
import org.hurricanegames.packetholograms.holograms.HologramController;
import org.hurricanegames.packetholograms.integrations.PlaceholderAPIIntergration;
import org.hurricanegames.pluginlib.commands.BukkitCommandExecutor;

public class PacketHologramsPlugin extends JavaPlugin {

	private HologramController controller;

	public HologramController getController() {
		return controller;
	}

	@Override
	public void onEnable() {
		getLogger().log(Level.INFO, "Using entity id generator " + EntityIdGenerator.INSTANCE.getClass().getSimpleName());
		PlaceholderAPIIntergration.init();
		controller = new HologramController(this);
		controller.initAndLoad();
		getCommand("holograms").setExecutor(new BukkitCommandExecutor(new PacketHologramsCommands(new PacketHologramsCommandHelper(this))));
	}

	@Override
	public void onDisable() {
		controller.save();
		controller.destroy();
	}

}
