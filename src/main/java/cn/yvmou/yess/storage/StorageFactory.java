package cn.yvmou.yess.storage;

import cn.yvmou.yess.Y;

public class StorageFactory {
    public static PluginStorage createStorage(Y plugin) {
        String storageType = plugin.getConfig().getString("storage.type", "yaml").toLowerCase();

        return switch (storageType) {
            case "mysql", "sqlite", "yaml" -> new YamlStorage(plugin);
            default -> new YamlStorage(plugin);
        };
    }
}
