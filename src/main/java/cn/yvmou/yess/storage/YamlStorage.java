package cn.yvmou.yess.storage;

import cn.yvmou.yess.YEss;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YamlStorage implements PluginStorage {
    private final YEss plugin;
    private YamlConfiguration dataConfig;
    private File dataFolder;
    private File dataFile;

    public YamlStorage(YEss plugin) {
        this.plugin = plugin;
        this.init();
    }

    @Override
    public void init() {
        dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        dataFile = new File(dataFolder, "data.yml");

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void setGlowing(UUID playerId, boolean glowing) {
        Set<UUID> glowingPlayers = getAllGlowingPlayers();
        if (glowing) {
            glowingPlayers.add(playerId);
        } else {
            glowingPlayers.remove(playerId);
        }
        dataConfig.set("glowing-players", glowingPlayers.stream()
                .map(UUID::toString)
                .toList());
        save();
    }

    @Override
    public boolean isGlowing(UUID playerId) {
        return getAllGlowingPlayers().contains(playerId);
    }

    @Override
    public Set<UUID> getAllGlowingPlayers() {
        Set<UUID> glowingPlayers = new HashSet<>();
        if (dataConfig.contains("glowing-players")) {
            for (String uuidString : dataConfig.getStringList("glowing-players")) {
                try {
                    glowingPlayers.add(UUID.fromString(uuidString));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("无效的UUID格式: " + uuidString);
                }
            }
        }
        return glowingPlayers;
    }

    @Override
    public void clearAll() {
        dataConfig.set("glowing-players", null);
        save();
    }

    private void save() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存发光状态数据: " + e.getMessage());
        }
    }
}

