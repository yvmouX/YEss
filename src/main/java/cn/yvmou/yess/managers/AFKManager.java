package cn.yvmou.yess.managers;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.utils.LoggerUtils;
import cn.yvmou.yess.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AFKManager {
    private Map<UUID, Long> lastMoveTime = new HashMap<>();
    private Map<UUID, Location> lastLocation = new HashMap<>();

    public AFKManager() {
        SchedulerUtils.runTaskTimerAsynchronously(Y.getInstance(), this::checkAFKPlayers, 20L, 20L);
    }

    public Map<UUID, Long> getLastMoveTime() {
        return lastMoveTime;
    }

    public Map<UUID, Location> getLastLocation() {
        return lastLocation;
    }


    private void checkAFKPlayers() {
        if (!Y.getInstance().getConfig().getBoolean("AFK.enable")) return;

        List<String> disableWorlds = Y.getInstance().getConfig().getStringList("AFK.disableWorlds");
        int afkTime = Y.getInstance().getConfig().getInt("AFK.afkTime");
        String toWorld = Y.getInstance().getConfig().getString("AFK.toWorld");
        List<String> effects = Y.getInstance().getConfig().getStringList("AFK.effects");
        List<String> commands = Y.getInstance().getConfig().getStringList("AFK.commands");

        if (toWorld != null) toWorld = toWorld.toLowerCase();

        long now = System.currentTimeMillis();
        long newAfkTime = afkTime * 1000L;
        for (Player player : Bukkit.getOnlinePlayers()) {
            // 禁用afk的世界
            if (disableWorlds.contains(player.getWorld().getName())) return;
            // 玩家位于afk的世界禁用
            if (toWorld != null && toWorld.equalsIgnoreCase(player.getWorld().getName())) return;

            UUID uuid = player.getUniqueId();
            if (lastMoveTime.containsKey(uuid)) {
                long lastTime = lastMoveTime.get(uuid);
                if (now - lastTime > newAfkTime) {
                    // toWorld不为空 且 玩家不位于 toWorld 启用传送
                    if (toWorld != null)
                        teleportWorld(player, toWorld);

                    // 给予玩家效果
                    giveEffects(player, effects);

                    // 执行命令
                    executeCommands(player, commands);
                }
            } else {
                // 设置未包含在MAP的玩家的初始值
                lastMoveTime.put(uuid, System.currentTimeMillis());
                lastLocation.put(uuid, player.getLocation());
            }
        }
    }

    private void giveEffects(Player player, List<String> effects) {
        if (effects.isEmpty()) return;
        try {
            for (String effect : effects) {
                PotionEffectType type = PotionEffectType.getByName(effect);

                String[] parts = effect.split(":");

                if (!(parts.length == 2)) return;

                int amplifier = Integer.parseInt(parts[1]);

                if (type == null) continue;

                player.addPotionEffect(new PotionEffect(
                        type,
                        Integer.MAX_VALUE,
                        amplifier,
                        false,
                        false,
                        true
                ));
            }
        } catch (Exception e) {
            LoggerUtils.error("应用效果时出错: " + effects);
            e.printStackTrace();
        }
    }

    private void executeCommands(Player player, List<String> commands) {
        if (commands.isEmpty()) return;
        for (String command : commands) {
            try {
                // 替换占位符
                command = command.replace("%player%", player.getName());

                // 控制台执行命令
                String finalCommand = command;
                SchedulerUtils.runTask(Y.getInstance(), () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
                });
            } catch (Exception e) {
                LoggerUtils.error("执行命令时出错: " + command);
                e.printStackTrace();
            }
        }
    }

    private void teleportWorld(Player player, String toWorld) {
        Location nowLoc = player.getLocation();
        Location lastLoc = lastLocation.get(player.getUniqueId());
        if (nowLoc.equals(lastLoc)) {
            World w = Bukkit.getWorld(toWorld.toLowerCase());
            if (w == null) return;
            SchedulerUtils.runTask(Y.getInstance(), () -> {
                player.teleport(w.getSpawnLocation());
                player.sendMessage("§c你因为长时间不动被传送到了AFK区域!");
            });
        }
    }
}
