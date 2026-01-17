package dev.m4trix.framework.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public final class CommandRegistry {

    private final JavaPlugin plugin;
    private final Set<String> registeredCommands;
    private final Map<String, Set<String>> commandAliases;

    public CommandRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredCommands = new HashSet<>();
        this.commandAliases = new HashMap<>();
    }

    public boolean register(String name, CommandExecutor executor) {
        if (name == null || name.trim().isEmpty()) {
            plugin.getLogger().warning("Cannot register command with null or empty name");
            return false;
        }

        if (executor == null) {
            plugin.getLogger().warning("Cannot register null executor for command: " + name);
            return false;
        }

        PluginCommand command = plugin.getCommand(name);
        
        if (command == null) {
            plugin.getLogger().log(Level.WARNING, "Command not declared in plugin.yml: " + name);
            return false;
        }

        command.setExecutor(executor);
        
        if (executor instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) executor);
        }

        registeredCommands.add(name);
        
        List<String> aliases = command.getAliases();
        if (!aliases.isEmpty()) {
            commandAliases.put(name, new HashSet<>(aliases));
        }
        
        return true;
    }

    public boolean unregister(String name) {
        if (name == null || !registeredCommands.contains(name)) {
            return false;
        }

        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setExecutor(null);
            command.setTabCompleter(null);
        }

        registeredCommands.remove(name);
        commandAliases.remove(name);
        return true;
    }

    public boolean isRegistered(String name) {
        return registeredCommands.contains(name);
    }

    public Set<String> getAliases(String command) {
        Set<String> aliases = commandAliases.get(command);
        return aliases != null ? Collections.unmodifiableSet(aliases) : Collections.emptySet();
    }

    public Set<String> getRegisteredCommands() {
        return Collections.unmodifiableSet(registeredCommands);
    }

    public int count() {
        return registeredCommands.size();
    }
}