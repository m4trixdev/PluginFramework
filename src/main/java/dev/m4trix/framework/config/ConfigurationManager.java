package dev.m4trix.framework.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class ConfigurationManager {

    private final JavaPlugin plugin;
    private final Map<String, ConfigEntry> configurations;

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configurations = new ConcurrentHashMap<>();
    }

    public FileConfiguration load(String name) {
        return load(name, true);
    }

    public FileConfiguration load(String name, boolean saveDefault) {
        String normalizedName = normalizeName(name);
        
        ConfigEntry cached = configurations.get(normalizedName);
        if (cached != null) {
            return cached.configuration;
        }

        File file = new File(plugin.getDataFolder(), normalizedName);
        
        if (!file.exists()) {
            if (saveDefault && hasResource(normalizedName)) {
                saveResource(normalizedName, file);
            } else {
                createEmptyFile(file);
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configurations.put(normalizedName, new ConfigEntry(config, file));
        
        return config;
    }

    public FileConfiguration get(String name) {
        String normalizedName = normalizeName(name);
        ConfigEntry entry = configurations.get(normalizedName);
        return entry != null ? entry.configuration : null;
    }

    public boolean save(String name) {
        String normalizedName = normalizeName(name);
        ConfigEntry entry = configurations.get(normalizedName);

        if (entry == null) {
            plugin.getLogger().warning("Cannot save config that hasn't been loaded: " + normalizedName);
            return false;
        }

        try {
            entry.configuration.save(entry.file);
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config: " + normalizedName, e);
            return false;
        }
    }

    public boolean reload(String name) {
        String normalizedName = normalizeName(name);
        ConfigEntry entry = configurations.get(normalizedName);
        
        if (entry == null) {
            plugin.getLogger().warning("Cannot reload config that hasn't been loaded: " + normalizedName);
            return false;
        }

        if (!entry.file.exists()) {
            plugin.getLogger().warning("Config file does not exist: " + normalizedName);
            return false;
        }

        try {
            FileConfiguration reloaded = YamlConfiguration.loadConfiguration(entry.file);
            configurations.put(normalizedName, new ConfigEntry(reloaded, entry.file));
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to reload config: " + normalizedName, e);
            return false;
        }
    }

    public void reloadAll() {
        for (String name : configurations.keySet()) {
            reload(name);
        }
    }

    public boolean unload(String name) {
        String normalizedName = normalizeName(name);
        return configurations.remove(normalizedName) != null;
    }

    public void unloadAll() {
        configurations.clear();
    }

    private String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Config name cannot be null or empty");
        }
        return name.endsWith(".yml") ? name : name + ".yml";
    }

    private boolean hasResource(String name) {
        try (InputStream stream = plugin.getResource(name)) {
            return stream != null;
        } catch (IOException e) {
            return false;
        }
    }

    private void saveResource(String name, File destination) {
        try {
            destination.getParentFile().mkdirs();
            try (InputStream in = plugin.getResource(name)) {
                if (in != null) {
                    Files.copy(in, destination.toPath());
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save default resource: " + name, e);
        }
    }

    private void createEmptyFile(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create config file: " + file.getName(), e);
        }
    }

    private static final class ConfigEntry {
        private final FileConfiguration configuration;
        private final File file;

        ConfigEntry(FileConfiguration configuration, File file) {
            this.configuration = configuration;
            this.file = file;
        }
    }
}