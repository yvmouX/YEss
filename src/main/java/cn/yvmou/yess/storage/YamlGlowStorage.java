package cn.yvmou.yess.storage;

import cn.yvmou.yess.YEss;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YamlGlowStorage implements GlowStorage {
    private final YEss plugin;
    private YamlConfiguration dataConfig;
    private File dataFile;

    public YamlGlowStorage(YEss plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        dataFile = new File(plugin.getDataFolder(), "glow_data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("glow_data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
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

