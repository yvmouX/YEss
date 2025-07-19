package cn.yvmou.yess.events;

import cn.yvmou.yess.Y;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.List;

public class PlayerPortalListener implements Listener {
    private final Y plugin;
    public PlayerPortalListener(Y plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerEnterNether(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        List<String> worlds = plugin.getConfig().getStringList("disableNetherPortal");
        for (String world : worlds) {
            if (player.getWorld().getName().equalsIgnoreCase(world)) {
                event.setCancelled(true);
            }
        }
    }
}
