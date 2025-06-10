package cn.yvmou.yess.events;

import cn.yvmou.yess.Y;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerAFKListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            Y.getAFKManager().getLastMoveTime().put(player.getUniqueId(), System.currentTimeMillis());
            Y.getAFKManager().getLastLocation().put(player.getUniqueId(), to);
        };
    }
}
