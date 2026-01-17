package br.com.m4trixdev;

import br.com.m4trixdev.command.FrameworkCommand;
import br.com.m4trixdev.listener.ConnectionListener;
import dev.m4trix.framework.command.CommandRegistry;
import dev.m4trix.framework.config.ConfigurationManager;
import dev.m4trix.framework.core.BasePlugin;
import dev.m4trix.framework.event.EventRegistry;
import dev.m4trix.framework.service.CacheService;
import dev.m4trix.framework.service.ServiceManager;
import org.bukkit.configuration.file.FileConfiguration;

public final class Main extends BasePlugin {

    private CacheService cacheService;

    @Override
    protected void initialize() {
        ConfigurationManager configManager = getConfigManager();
        FileConfiguration config = configManager.load("config.yml");
        
        if (config != null) {
            boolean debug = config.getBoolean("settings.debug", false);
            if (debug) {
                getLogger().info("Debug mode enabled");
            }
        }
    }

    @Override
    protected void registerCommands(CommandRegistry registry) {
        registry.register("framework", new FrameworkCommand(this));
    }

    @Override
    protected void registerEvents(EventRegistry registry) {
        registry.register(new ConnectionListener(this));
    }

    @Override
    protected void registerServices(ServiceManager manager) {
        this.cacheService = new CacheService();
        manager.register(cacheService);
    }

    @Override
    protected void shutdown() {
        getLogger().info("Cleaning up resources");
    }

    public CacheService getCacheService() {
        return cacheService;
    }
}