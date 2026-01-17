package dev.m4trix.framework.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerUtil {

    private LoggerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void info(JavaPlugin plugin, String message) {
        plugin.getLogger().info(message);
    }

    public static void warning(JavaPlugin plugin, String message) {
        plugin.getLogger().warning(message);
    }

    public static void severe(JavaPlugin plugin, String message) {
        plugin.getLogger().severe(message);
    }

    public static void error(JavaPlugin plugin, String message, Throwable throwable) {
        plugin.getLogger().log(Level.SEVERE, message, throwable);
    }

    public static void debug(JavaPlugin plugin, String message, boolean debugMode) {
        if (debugMode) {
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }

    public static Logger getLogger(JavaPlugin plugin) {
        return plugin.getLogger();
    }
}