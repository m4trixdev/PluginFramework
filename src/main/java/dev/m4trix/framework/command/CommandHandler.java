package dev.m4trix.framework.command;

import org.bukkit.command.CommandSender;

public interface CommandHandler {

    boolean execute(CommandSender sender, String[] args);

    default String getPermission() {
        return null;
    }

    default String getUsage() {
        return "";
    }
}