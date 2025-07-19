package cn.yvmou.yess.storage;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.tools.LoggerTools;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YamlStorage implements Storage{
    private final Y plugin;
    private File dataFolder;
    private File dataFile;
    private YamlConfiguration dataConfig;
    private final LoggerTools logger = Y.getYLib().getLoggerTools();

    public YamlStorage(Y plugin) {
        this.plugin = plugin;
        init();
    }

    @Override
    public void init() {
        dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        dataFile = new File(dataFolder, "player_data.yml");

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void saveData(UUID uuid, String type, T data) {
        dataConfig.set("player_data." + uuid + ".name", Bukkit.getOfflinePlayer(uuid).getName());
        if (data == null) {
            dataConfig.set("player_data." + uuid + "." + type, null);
            save();
            return;
        }
        if (data instanceof Boolean || data instanceof String || data instanceof Integer || data instanceof Double) {
            dataConfig.set("player_data." + uuid + "." + type, data);
        } else {
            logger.error("不支持的数据类型" + data.getClass().getName());
        }
        save();
    }

    @Override
    public <T> T loadData(UUID uuid, String type, Class<T> clazz, T defaultValue) {
        Object data = dataConfig.get("player_data." + uuid + "." + type);
        if (data == null) {
            return defaultValue;
        }
        if (clazz.isInstance(data)) {
            return clazz.cast(data);
        } else {
            logger.warn("数据类型不匹配: " + type + "，实际类型: " + data.getClass().getName());
            return null;
        }
    }

    @Override
    public void close() {
    }


    private void save() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存玩家数据: " + e.getMessage());
        }
    }
}
