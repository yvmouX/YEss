package cn.yvmou.yess.events;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.storage.PlayerDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {
    private final PlayerDataStorage playerDataStorage = Y.getPlayerStorage();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player player)) return;

        if (playerDataStorage.getPlayerData(player).equals(playerDataStorage.getPlayerData(damager))) {
            event.setCancelled(true);
        }
    }
}
