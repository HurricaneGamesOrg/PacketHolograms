package org.hurricanegames.packetholograms.holograms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.util.Vector;

public class Hologram {

	private final String name;
	private final String world;
	private final Vector location;
	private final List<String> lines;

	public Hologram(String name, String world, Vector location, List<String> lines) {
		this.name = name;
		this.world = world;
		this.location = location.clone();
		this.lines = new ArrayList<>(lines);
	}

	public final String getName() {
		return name;
	}

	public final String getWorld() {
		return world;
	}

	public final Vector getLocation() {
		return location.clone();
	}

	public final List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}

	protected static final String world_path = "world";
	protected static final String location_path = "location";
	protected static final String lines_path = "lines";

	public ConfigurationSection toConfigurationSection() {
		MemoryConfiguration section = new MemoryConfiguration();
		section.set(world_path, world);
		section.set(location_path, location);
		section.set(lines_path, lines);
		return section;
	}

	public static Hologram fromConfigurationSection(String name, ConfigurationSection section) {
		String world = section.getString(world_path);
		Vector location = section.getVector(location_path);
		List<String> lines = section.getStringList(lines_path);
		return new Hologram(name, world, location, lines);
	}

}
