package dev.m4trix.framework.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CacheService implements Service {

    private final Map<String, CacheEntry<?>> cache;
    private volatile boolean running;

    public CacheService() {
        this.cache = new ConcurrentHashMap<>();
        this.running = false;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
        cache.clear();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public <T> void put(String key, T value, long ttl) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttl));
    }

    public <T> void put(String key, T value) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }
        cache.put(key, new CacheEntry<>(value, -1));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        if (key == null) {
            return Optional.empty();
        }

        CacheEntry<?> entry = cache.get(key);
        
        if (entry == null) {
            return Optional.empty();
        }

        if (entry.isExpired()) {
            cache.remove(key);
            return Optional.empty();
        }

        return Optional.of((T) entry.value);
    }

    public <T> T getOrCompute(String key, Supplier<T> supplier) {
        if (key == null || supplier == null) {
            throw new IllegalArgumentException("Key and supplier cannot be null");
        }

        return this.<T>get(key).orElseGet(() -> {
            T value = supplier.get();
            if (value != null) {
                put(key, value);
            }
            return value;
        });
    }

    public boolean contains(String key) {
        if (key == null) {
            return false;
        }
        
        CacheEntry<?> entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        
        return true;
    }

    public void invalidate(String key) {
        if (key != null) {
            cache.remove(key);
        }
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        cleanExpired();
        return cache.size();
    }

    public Set<String> keys() {
        cleanExpired();
        return cache.keySet();
    }

    private void cleanExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static final class CacheEntry<T> {
        private final T value;
        private final long expireTime;

        CacheEntry(T value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }
}