package dev.m4trix.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    private final Map<String, CommandHandler> subCommands;

    public BaseCommand() {
        this.subCommands = new LinkedHashMap<>();
    }

    protected final void registerSubCommand(String name, CommandHandler handler) {
        if (name == null || handler == null) {
            throw new IllegalArgumentException("Subcommand name and handler cannot be null");
        }
        subCommands.put(name.toLowerCase(), handler);
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            CommandHandler handler = subCommands.get(args[0].toLowerCase());
            
            if (handler != null) {
                String permission = handler.getPermission();
                
                if (permission != null && !sender.hasPermission(permission)) {
                    sender.sendMessage("§cYou don't have permission to use this command.");
                    return true;
                }

                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                return handler.execute(sender, subArgs);
            }
        }

        return executeDefault(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            String input = args[0].toLowerCase();
            
            for (Map.Entry<String, CommandHandler> entry : subCommands.entrySet()) {
                String subCmd = entry.getKey();
                CommandHandler handler = entry.getValue();
                String permission = handler.getPermission();
                
                if (permission == null || sender.hasPermission(permission)) {
                    if (subCmd.startsWith(input)) {
                        completions.add(subCmd);
                    }
                }
            }
            
            return completions;
        }

        if (args.length > 1) {
            CommandHandler handler = subCommands.get(args[0].toLowerCase());
            if (handler instanceof TabCompleter) {
                return ((TabCompleter) handler).onTabComplete(sender, command, alias, args);
            }
        }

        return Collections.emptyList();
    }

    protected abstract boolean executeDefault(CommandSender sender, String[] args);

    protected final Map<String, CommandHandler> getSubCommands() {
        return Collections.unmodifiableMap(subCommands);
    }
}