package cn.yvmou.yess.storage;

import cn.yvmou.yess.YEss;

public class StorageFactory {
    public static PluginStorage createStorage(YEss plugin) {
        String storageType = plugin.getConfig().getString("storage.type", "yaml").toLowerCase();

        return switch (storageType) {
            case "mysql", "sqlite", "yaml" -> new YamlStorage(plugin);
            default -> new YamlStorage(plugin);
        };
    }
}
