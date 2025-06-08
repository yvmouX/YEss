package cn.yvmou.yess.utils;

import cn.yvmou.yess.YEss;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {
    public static void runTask(Plugin plugin, Runnable runnable) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runNextTick(wrappedTask -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runAsync(wrappedTask -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static void runTaskLater(final Plugin plugin, final Runnable runnable, long delay) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runLater(runnable, delay);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runTaskTimerAsynchronously(final Plugin plugin, final Runnable runnable, long delay, long period) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runTimerAsync(runnable, delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        }
    }

    public static void runTaskLaterAsynchronously(final Plugin plugin, final Runnable runnable, long delay) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runLaterAsync(runnable, delay);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static void scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long delay) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runLater(runnable, delay);
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
        }
    }

    public static void scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runTimer(runnable, delay, period);
        } else {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, period);
        }
    }


}
