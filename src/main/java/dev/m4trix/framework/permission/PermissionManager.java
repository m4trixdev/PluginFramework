package dev.m4trix.framework.permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class PermissionManager {

    private final JavaPlugin plugin;
    private final Map<String, Permission> registeredPermissions;

    public PermissionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredPermissions = new HashMap<>();
    }

    public void register(String permission, PermissionDefault defaultValue) {
        if (permission == null || permission.isEmpty()) {
            return;
        }

        if (registeredPermissions.containsKey(permission)) {
            return;
        }

        Permission perm = new Permission(permission, defaultValue);
        plugin.getServer().getPluginManager().addPermission(perm);
        registeredPermissions.put(permission, perm);
    }

    public void register(String permission, PermissionDefault defaultValue, String description) {
        if (permission == null || permission.isEmpty()) {
            return;
        }

        if (registeredPermissions.containsKey(permission)) {
            return;
        }

        Permission perm = new Permission(permission, description, defaultValue);
        plugin.getServer().getPluginManager().addPermission(perm);
        registeredPermissions.put(permission, perm);
    }

    public void unregister(String permission) {
        Permission perm = registeredPermissions.remove(permission);
        if (perm != null) {
            plugin.getServer().getPluginManager().removePermission(perm);
        }
    }

    public void unregisterAll() {
        for (Permission perm : registeredPermissions.values()) {
            plugin.getServer().getPluginManager().removePermission(perm);
        }
        registeredPermissions.clear();
    }

    public boolean has(CommandSender sender, String permission) {
        return sender != null && permission != null && sender.hasPermission(permission);
    }

    public boolean has(Player player, String permission) {
        return player != null && permission != null && player.hasPermission(permission);
    }

    public boolean hasAny(CommandSender sender, String... permissions) {
        if (sender == null || permissions == null) {
            return false;
        }

        for (String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAll(CommandSender sender, String... permissions) {
        if (sender == null || permissions == null) {
            return false;
        }

        for (String permission : permissions) {
            if (!sender.hasPermission(permission)) {
                return false;
            }
        }

        return true;
    }
}