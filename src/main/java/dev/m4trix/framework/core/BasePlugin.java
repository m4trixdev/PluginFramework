package dev.m4trix.framework.core;

import dev.m4trix.framework.command.CommandRegistry;
import dev.m4trix.framework.config.ConfigurationManager;
import dev.m4trix.framework.event.EventRegistry;
import dev.m4trix.framework.service.ServiceManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public abstract class BasePlugin extends JavaPlugin {

    private ConfigurationManager configManager;
    private CommandRegistry commandRegistry;
    private EventRegistry eventRegistry;
    private ServiceManager serviceManager;

    private volatile boolean initialized = false;

    @Override
    public final void onEnable() {
        try {
            initializeFramework();
            initialize();
            registerComponents();
            postInitialize();
            initialized = true;
            getLogger().info("Plugin initialized successfully");
        } catch (Throwable t) {
            getLogger().log(Level.SEVERE, "Failed to initialize plugin", t);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void onDisable() {
        if (!initialized) {
            return;
        }

        try {
            preShutdown();
            shutdownFramework();
            shutdown();
            getLogger().info("Plugin shutdown completed");
        } catch (Throwable t) {
            getLogger().log(Level.SEVERE, "Error during shutdown", t);
        } finally {
            initialized = false;
        }
    }

    private void initializeFramework() {
        this.configManager = new ConfigurationManager(this);
        this.commandRegistry = new CommandRegistry(this);
        this.eventRegistry = new EventRegistry(this);
        this.serviceManager = new ServiceManager(this);
    }

    private void registerComponents() {
        registerCommands(commandRegistry);
        registerEvents(eventRegistry);
        registerServices(serviceManager);
    }

    private void shutdownFramework() {
        if (serviceManager != null) {
            serviceManager.stopAll();
        }
        if (eventRegistry != null) {
            eventRegistry.unregisterAll();
        }
    }

    protected abstract void initialize();

    protected abstract void registerCommands(CommandRegistry registry);

    protected abstract void registerEvents(EventRegistry registry);

    protected abstract void registerServices(ServiceManager manager);

    protected void postInitialize() {
    }

    protected void preShutdown() {
    }

    protected abstract void shutdown();

    public final ConfigurationManager getConfigManager() {
        return configManager;
    }

    public final CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public final EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public final ServiceManager getServiceManager() {
        return serviceManager;
    }

    public final boolean isInitialized() {
        return initialized;
    }
}