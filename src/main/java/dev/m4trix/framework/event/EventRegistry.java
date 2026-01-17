package dev.m4trix.framework.event;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EventRegistry {

    private final JavaPlugin plugin;
    private final Set<Listener> registeredListeners;

    public EventRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredListeners = new HashSet<>();
    }

    public boolean register(Listener listener) {
        if (listener == null) {
            plugin.getLogger().warning("Cannot register null listener");
            return false;
        }

        if (registeredListeners.contains(listener)) {
            plugin.getLogger().warning("Listener already registered: " + listener.getClass().getName());
            return false;
        }

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        registeredListeners.add(listener);
        return true;
    }

    public boolean unregister(Listener listener) {
        if (listener == null || !registeredListeners.contains(listener)) {
            return false;
        }

        HandlerList.unregisterAll(listener);
        registeredListeners.remove(listener);
        return true;
    }

    public void unregisterAll() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
        registeredListeners.clear();
    }

    public boolean isRegistered(Listener listener) {
        return registeredListeners.contains(listener);
    }

    public Set<Listener> getRegisteredListeners() {
        return Collections.unmodifiableSet(registeredListeners);
    }

    public int count() {
        return registeredListeners.size();
    }
}