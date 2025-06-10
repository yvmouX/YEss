package cn.yvmou.yess.storage;

import cn.yvmou.yess.Y;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class PlayerDataStorage {
    private final Y plugin;
    private PersistentDataContainer data;
    private String _data;

    public PlayerDataStorage(Y plugin) {
        this.plugin = plugin;
    }

    public void setPlayerData(Player leader, Player member, boolean isInvited) {
        data = member.getPersistentDataContainer();

        if (isInvited) {
            data.set(new NamespacedKey(plugin, "team"), PersistentDataType.STRING, leader + "-invited");
        } else {
            data.set(new NamespacedKey(plugin, "team"), PersistentDataType.STRING, leader.getName());
        }

    }

    public void removePlayerData(Player member) {
        data = member.getPersistentDataContainer();
        data.remove(new NamespacedKey(plugin, "team"));
    }

    public void removePlayerData(OfflinePlayer member) {
        if (member.getPlayer() == null) return;

        data = member.getPlayer().getPersistentDataContainer();
        data.remove(new NamespacedKey(plugin, "team"));
    }

    public String getPlayerData(Player player) {
        data = player.getPersistentDataContainer();
        try {
            _data = data.get(new NamespacedKey(plugin, "team"), PersistentDataType.STRING);
        } catch (Exception e) {
            return null;
        }
        return _data;
    }

    public String getPlayerData(OfflinePlayer player) {
        if (player.getPlayer() == null) return null;

        try {
            _data = data.get(new NamespacedKey(plugin, "team"), PersistentDataType.STRING);
        } catch (Exception e) {
            return null;
        }
        return _data;
    }

    public boolean isInvited(Player player) {
        return getPlayerData(player).contains("-invited");
    }
}
