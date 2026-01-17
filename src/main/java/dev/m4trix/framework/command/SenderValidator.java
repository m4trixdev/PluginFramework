package dev.m4trix.framework.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class SenderValidator {

    private SenderValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }

    public static Player requirePlayer(CommandSender sender) {
        if (!isPlayer(sender)) {
            throw new IllegalStateException("Command sender must be a player");
        }
        return (Player) sender;
    }

    public static Player asPlayer(CommandSender sender) {
        return isPlayer(sender) ? (Player) sender : null;
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender != null && permission != null && sender.hasPermission(permission);
    }

    public static boolean validatePlayer(CommandSender sender, String message) {
        if (!isPlayer(sender)) {
            sender.sendMessage(message != null ? message : "§cThis command can only be used by players");
            return false;
        }
        return true;
    }

    public static boolean validatePermission(CommandSender sender, String permission, String message) {
        if (!hasPermission(sender, permission)) {
            sender.sendMessage(message != null ? message : "§cYou don't have permission to use this command");
            return false;
        }
        return true;
    }

    public static boolean validateArgs(String[] args, int required, CommandSender sender, String usage) {
        if (args.length < required) {
            if (usage != null) {
                sender.sendMessage("§cUsage: " + usage);
            }
            return false;
        }
        return true;
    }
}