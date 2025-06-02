package cn.yvmou.yess.storage;

import cn.yvmou.yess.YEss;

public class StorageFactory {
    public static GlowStorage createStorage(YEss plugin) {
        String storageType = plugin.getConfig().getString("storage.type", "yaml").toLowerCase();

        return switch (storageType) {
            case "mysql" -> new MySQLGlowStorage(plugin);
            case "yaml" -> new YamlGlowStorage(plugin);
            default -> new SQLiteGlowStorage(plugin);
        };
    }
}
