package dev.m4trix.framework.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;

public final class PlayerUtil {

    private PlayerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayerExact(name));
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public static void sendMessage(Player player, String message) {
        if (player != null && message != null) {
            player.sendMessage(TextUtil.color(message));
        }
    }

    public static void sendMessage(Player player, Component component) {
        if (player != null && component != null) {
            player.sendMessage(component);
        }
    }

    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, message));
    }

    public static void broadcast(Component component) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, component));
    }

    public static boolean hasPermission(Player player, String permission) {
        return player != null && permission != null && player.hasPermission(permission);
    }
}