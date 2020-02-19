package org.hurricanegames.packetholograms.integrations;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIIntergration implements Listener {

	private static final PlaceholderAPIIntergration instance = new PlaceholderAPIIntergration();

	protected boolean enabled = false;

	public static String setBracketPlaceholders(OfflinePlayer player, String text) {
		if (instance.enabled) {
			return PlaceholderAPI.setBracketPlaceholders(player, text);
		} else {
			return text;
		}
	}

	protected static final String pluginName = "PlaceholderAPI";

	@EventHandler
	protected void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getName().equals(pluginName)) {
			enabled = true;
		}
	}

	@EventHandler
	protected void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().getName().equals(pluginName)) {
			enabled = false;
		}
	}

	private static boolean init = false;
	public static void init() {
		if (init) {
			return;
		}
		Plugin plugin = JavaPlugin.getProvidingPlugin(PlaceholderAPIIntergration.class);
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		pluginManager.registerEvents(instance, plugin);
		if (pluginManager.isPluginEnabled(pluginName)) {
			instance.enabled = true;
		}
	}

}
