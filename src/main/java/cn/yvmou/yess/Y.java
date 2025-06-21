package cn.yvmou.yess;

import cn.yvmou.yess.managers.GiftM;
import cn.yvmou.yess.expansion.PapiExpansion;
import cn.yvmou.yess.managers.TeamM;
import cn.yvmou.yess.managers.GlowM;
import cn.yvmou.yess.storage.Storage;
import cn.yvmou.yess.utils.LoggerUtils;
import cn.yvmou.yess.utils.Metrics;
import cn.yvmou.yess.utils.manager.CommandManager;
import cn.yvmou.yess.storage.StorageType;
import cn.yvmou.yess.utils.manager.ListenerManager;
import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Y extends JavaPlugin {
    private static Y instance;
    private static FoliaLib foliaLib;
    private static GlowM glowM;
    private static Storage storage;
    private static GiftM giftM;
    private static TeamM teamM;

    public static Plugin getInstance() {
        return instance;
    }
    public static FoliaLib getFoliaLib() {return foliaLib;}
    public static Storage getStorage() {return storage;}
    public static GlowM glowM() { return glowM; }
    public static GiftM getGiftM() {return giftM;}
    public static TeamM getTeamM() {return teamM;}

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // 初始化
        instance = this;

        foliaLib = new FoliaLib(this);

        storage = StorageType.createStorage(this); // 初始化插件存储

        giftM = new GiftM(this); // 初始化礼包管理器
        teamM = new TeamM(this, storage);
        glowM = new GlowM(storage);

        new CommandManager(this).registerCommands();
        new ListenerManager(this).registerListener();

        // HOOK
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansion(this).register(); //
            LoggerUtils.info(ChatColor.BLUE + "成功挂钩PlaceholderAPI");
        }
        LoggerUtils.info(ChatColor.GREEN + "插件加载成功！");
        // 检测配置文件是否更新
        checkConfigVersion();
        Metrics metrics = new Metrics(this, 26229);
    }

    @Override
    public void onDisable() {
        // 关闭存储系统
        if (storage != null) {storage.close();}

        LoggerUtils.info(ChatColor.RED + "插件卸载成功！");
    }

    private void checkConfigVersion() {
        String pluginVersion = getDescription().getVersion();
        String configVersion = getConfig().getString("version", "none");

        if (!configVersion.equals(pluginVersion)) {
            // 备份旧配置
            File configFile = new File(getDataFolder(), "config.yml");
            File backupFile = new File(getDataFolder(), "config_old_v" + configVersion + ".yml");
            if (configFile.exists()) {
                configFile.renameTo(backupFile);
            }

            // 重新加载默认配置
            saveDefaultConfig();

            // 合并用户自定义的配置
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);
            YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(backupFile);

            for (String key : oldConfig.getKeys(true)) {
                // 优先保留用户自定义（旧配置）内容
                newConfig.set(key, oldConfig.get(key));
            }

            // 更新版本号
            newConfig.set("version", pluginVersion);
            try {
                newConfig.save(configFile);
            } catch (Exception e) {
                LoggerUtils.error("保存新配置文件时出错: " + e.getMessage());
            }

            // 5. 重新加载配置到内存
            reloadConfig();

            LoggerUtils.info(ChatColor.GREEN + "配置文件已更新至 v" + pluginVersion + "，旧配置已备份。");
         }
    }
}