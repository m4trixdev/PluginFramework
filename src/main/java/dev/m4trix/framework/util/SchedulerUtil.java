package dev.m4trix.framework.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SchedulerUtil {

    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    private SchedulerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static BukkitTask sync(JavaPlugin plugin, Runnable task) {
        return SCHEDULER.runTask(plugin, task);
    }

    public static BukkitTask async(JavaPlugin plugin, Runnable task) {
        return SCHEDULER.runTaskAsynchronously(plugin, task);
    }

    public static BukkitTask syncLater(JavaPlugin plugin, Runnable task, long delay) {
        return SCHEDULER.runTaskLater(plugin, task, delay);
    }

    public static BukkitTask asyncLater(JavaPlugin plugin, Runnable task, long delay) {
        return SCHEDULER.runTaskLaterAsynchronously(plugin, task, delay);
    }

    public static BukkitTask syncRepeating(JavaPlugin plugin, Runnable task, long delay, long period) {
        return SCHEDULER.runTaskTimer(plugin, task, delay, period);
    }

    public static BukkitTask asyncRepeating(JavaPlugin plugin, Runnable task, long delay, long period) {
        return SCHEDULER.runTaskTimerAsynchronously(plugin, task, delay, period);
    }

    public static <T> CompletableFuture<T> supply(JavaPlugin plugin, Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        async(plugin, () -> {
            try {
                future.complete(supplier.get());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public static <T> void supplyThenAccept(JavaPlugin plugin, Supplier<T> supplier, Consumer<T> consumer) {
        async(plugin, () -> {
            T result = supplier.get();
            sync(plugin, () -> consumer.accept(result));
        });
    }

    public static void cancelTask(BukkitTask task) {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public static void cancelAll(JavaPlugin plugin) {
        SCHEDULER.cancelTasks(plugin);
    }
}