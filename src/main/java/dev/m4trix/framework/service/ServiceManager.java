package dev.m4trix.framework.service;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public final class ServiceManager {

    private final JavaPlugin plugin;
    private final Map<Class<? extends Service>, Service> services;

    public ServiceManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.services = new LinkedHashMap<>();
    }

    public <T extends Service> boolean register(T service) {
        if (service == null) {
            plugin.getLogger().warning("Cannot register null service");
            return false;
        }

        Class<? extends Service> serviceClass = service.getClass();
        
        if (services.containsKey(serviceClass)) {
            plugin.getLogger().warning("Service already registered: " + serviceClass.getName());
            return false;
        }

        try {
            service.start();
            services.put(serviceClass, service);
            plugin.getLogger().info("Service registered: " + service.getName());
            return true;
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Failed to start service: " + service.getName(), t);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T get(Class<T> serviceClass) {
        if (serviceClass == null) {
            return null;
        }
        return (T) services.get(serviceClass);
    }

    public <T extends Service> boolean stop(Class<T> serviceClass) {
        if (serviceClass == null) {
            return false;
        }

        Service service = services.get(serviceClass);
        if (service == null) {
            return false;
        }

        try {
            service.stop();
            services.remove(serviceClass);
            plugin.getLogger().info("Service stopped: " + service.getName());
            return true;
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Error stopping service: " + service.getName(), t);
            return false;
        }
    }

    public void stopAll() {
        for (Service service : services.values()) {
            try {
                service.stop();
                plugin.getLogger().info("Service stopped: " + service.getName());
            } catch (Throwable t) {
                plugin.getLogger().log(Level.SEVERE, "Error stopping service: " + service.getName(), t);
            }
        }
        services.clear();
    }

    public boolean isRegistered(Class<? extends Service> serviceClass) {
        return services.containsKey(serviceClass);
    }

    public Map<Class<? extends Service>, Service> getServices() {
        return Collections.unmodifiableMap(services);
    }

    public int count() {
        return services.size();
    }
}