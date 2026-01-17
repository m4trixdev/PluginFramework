package br.com.m4trixdev.listener;

import br.com.m4trixdev.Main;
import dev.m4trix.framework.service.CacheService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class ConnectionListener implements Listener {

    private static final long CACHE_TTL = 3600000L;
    
    private final Main plugin;

    public ConnectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CacheService cache = plugin.getCacheService();
        
        String key = "last_join:" + player.getUniqueId();
        cache.put(key, System.currentTimeMillis(), CACHE_TTL);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CacheService cache = plugin.getCacheService();
        
        String key = "last_quit:" + player.getUniqueId();
        cache.put(key, System.currentTimeMillis(), CACHE_TTL);
    }
}