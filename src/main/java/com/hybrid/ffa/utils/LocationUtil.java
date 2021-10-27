package com.hybrid.ffa.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationUtil {

    public static Location readLocation(ConfigurationSection section) {
        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                section.getInt("yaw"),
                section.getInt("pitch")
        );
    }

    public static void writeLocation(File file, String name, Location location) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(name, location.getWorld().getName());
        config.set(name, location.getX());
        config.set(name, location.getY());
        config.set(name, location.getZ());
        config.set(name, location.getYaw());
        config.set(name, location.getPitch());

        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}












