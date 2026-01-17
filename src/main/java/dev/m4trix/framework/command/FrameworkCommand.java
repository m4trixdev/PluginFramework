package br.com.m4trixdev.command;

import br.com.m4trixdev.Main;
import dev.m4trix.framework.command.BaseCommand;
import dev.m4trix.framework.command.CommandHandler;
import org.bukkit.command.CommandSender;

public final class FrameworkCommand extends BaseCommand {

    private final Main plugin;

    public FrameworkCommand(Main plugin) {
        this.plugin = plugin;
        registerSubCommand("reload", new ReloadHandler());
        registerSubCommand("info", new InfoHandler());
    }

    @Override
    protected boolean executeDefault(CommandSender sender, String[] args) {
        sender.sendMessage("§aFramework §7v" + plugin.getDescription().getVersion());
        sender.sendMessage("§7Use §e/framework info §7for more details");
        return true;
    }

    private final class ReloadHandler implements CommandHandler {
        
        @Override
        public boolean execute(CommandSender sender, String[] args) {
            long start = System.currentTimeMillis();
            plugin.getConfigManager().reloadAll();
            long elapsed = System.currentTimeMillis() - start;
            
            sender.sendMessage("§aConfiguration reloaded successfully");
            sender.sendMessage("§7Took §e" + elapsed + "ms");
            return true;
        }

        @Override
        public String getPermission() {
            return "framework.admin";
        }
    }

    private final class InfoHandler implements CommandHandler {
        
        @Override
        public boolean execute(CommandSender sender, String[] args) {
            sender.sendMessage("§a§lFramework Information");
            sender.sendMessage("§7Version: §e" + plugin.getDescription().getVersion());
            sender.sendMessage("§7Author: §e" + plugin.getDescription().getAuthors().get(0));
            sender.sendMessage("");
            sender.sendMessage("§7Registered Commands: §e" + plugin.getCommandRegistry().count());
            sender.sendMessage("§7Active Listeners: §e" + plugin.getEventRegistry().count());
            sender.sendMessage("§7Running Services: §e" + plugin.getServiceManager().count());
            sender.sendMessage("§7Cache Entries: §e" + plugin.getCacheService().size());
            return true;
        }
    }
}