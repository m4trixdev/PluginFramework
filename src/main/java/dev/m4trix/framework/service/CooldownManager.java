package dev.m4trix.framework.service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager implements Service {

    private final Map<String, Map<UUID, Long>> cooldowns;
    private volatile boolean running;

    public CooldownManager() {
        this.cooldowns = new ConcurrentHashMap<>();
        this.running = false;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
        cooldowns.clear();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public void set(String key, UUID player, Duration duration) {
        if (key == null || player == null || duration == null) {
            return;
        }

        cooldowns.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                 .put(player, System.currentTimeMillis() + duration.toMillis());
    }

    public void set(String key, UUID player, long millis) {
        if (key == null || player == null) {
            return;
        }

        cooldowns.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                 .put(player, System.currentTimeMillis() + millis);
    }

    public boolean has(String key, UUID player) {
        if (key == null || player == null) {
            return false;
        }

        Map<UUID, Long> keyCooldowns = cooldowns.get(key);
        if (keyCooldowns == null) {
            return false;
        }

        Long expiration = keyCooldowns.get(player);
        if (expiration == null) {
            return false;
        }

        if (System.currentTimeMillis() >= expiration) {
            keyCooldowns.remove(player);
            return false;
        }

        return true;
    }

    public Duration remaining(String key, UUID player) {
        if (key == null || player == null) {
            return Duration.ZERO;
        }

        Map<UUID, Long> keyCooldowns = cooldowns.get(key);
        if (keyCooldowns == null) {
            return Duration.ZERO;
        }

        Long expiration = keyCooldowns.get(player);
        if (expiration == null) {
            return Duration.ZERO;
        }

        long remaining = expiration - System.currentTimeMillis();
        if (remaining <= 0) {
            keyCooldowns.remove(player);
            return Duration.ZERO;
        }

        return Duration.ofMillis(remaining);
    }

    public void remove(String key, UUID player) {
        if (key == null || player == null) {
            return;
        }

        Map<UUID, Long> keyCooldowns = cooldowns.get(key);
        if (keyCooldowns != null) {
            keyCooldowns.remove(player);
        }
    }

    public void clear(String key) {
        if (key != null) {
            cooldowns.remove(key);
        }
    }

    public void clearAll() {
        cooldowns.clear();
    }

    public void cleanExpired() {
        long now = System.currentTimeMillis();
        cooldowns.values().forEach(map -> 
            map.entrySet().removeIf(entry -> entry.getValue() <= now)
        );
    }
}