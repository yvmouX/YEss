package cn.yvmou.yess.events;

import cn.yvmou.yess.YEss;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerListener implements Listener {
    private final YEss plugin;

    public PlayerListener(YEss plugin) { this.plugin = plugin; }

    /**
     * 玩家进入游戏时给予光芒效果
     *
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGlowStorage().isGlowing(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.GLOWING,
                    Integer.MAX_VALUE,
                    0,
                    false,
                    false,
                    true
            ));
        }
    }

    /**
     * 检测玩家位置执行命令
     *
     * @param e
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        Location to = e.getTo();

        if (to == null) return;
        if (isInRegion(to)) {
            List<String> command = plugin.getConfig().getStringList("portal.command");
            try {
                for (String cmd : command) {
                    String finalCmd = cmd.replace("%player%", playerName);
                    if (YEss.getFoliaLib().isFolia()) {
                        YEss.getFoliaLib().getScheduler().runNextTick(wrappedTask -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                        });
                    } else {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                        });
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
                player.sendMessage("§c传送执行失败!");
            }
        }
    }

    private Boolean isInRegion(Location loc) {
        String world = plugin.getConfig().getString("portal.world", "world");

        Location regionMin = new Location(
                Bukkit.getWorld(world),
                plugin.getConfig().getInt("portal.regionMin.x", 0),
                plugin.getConfig().getInt("portal.regionMin.y", 0),
                plugin.getConfig().getInt("portal.regionMin.z", 0)
        );
        Location regionMax = new Location(
                Bukkit.getWorld(world),
                plugin.getConfig().getInt("portal.regionMax.x", 0),
                plugin.getConfig().getInt("portal.regionMax.y", 0),
                plugin.getConfig().getInt("portal.regionMax.z", 0)
        );

        if (loc.getWorld() == null) return false;
        if (!loc.getWorld().equals(Bukkit.getWorld(world))) {return false;}

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        var is_x = x >= regionMin.getX() && x <= regionMax.getX();
        var is_y = y >= regionMin.getY() && y <= regionMax.getY();
        var is_z = z >= regionMin.getZ() && z <= regionMax.getZ();

        return is_x && is_y && is_z;
    }
}
