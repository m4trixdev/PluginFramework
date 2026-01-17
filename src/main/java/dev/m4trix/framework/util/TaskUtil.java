package dev.m4trix.framework.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtil {

    private TaskUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static BukkitTask run(JavaPlugin plugin, Runnable task) {
        return Bukkit.getScheduler().runTask(plugin, task);
    }

    public static BukkitTask runAsync(JavaPlugin plugin, Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public static BukkitTask runLater(JavaPlugin plugin, Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    public static BukkitTask runLaterAsync(JavaPlugin plugin, Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    public static BukkitTask runTimer(JavaPlugin plugin, Runnable task, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    public static BukkitTask runTimerAsync(JavaPlugin plugin, Runnable task, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
    }

    public static void cancel(BukkitTask task) {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public static void cancelAll(JavaPlugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}