package cn.yvmou.yess;

import cn.yvmou.yess.managers.GiftManager;
import cn.yvmou.yess.expansion.PapiExpansion;
import cn.yvmou.yess.utils.LoggerUtils;
import cn.yvmou.yess.utils.manager.CommandManager;
import cn.yvmou.yess.storage.PluginStorage;
import cn.yvmou.yess.storage.StorageFactory;
import cn.yvmou.yess.utils.manager.ListenerManager;
import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Y extends JavaPlugin {
    private static Y instance;
    private static FoliaLib foliaLib;
    private static PluginStorage pluginStorage;
    private static GiftManager giftManager;

    public static FoliaLib getFoliaLib() {return foliaLib;}
    public static PluginStorage getPluginStorage() {return pluginStorage;}
    public static GiftManager getGiftManager() {return giftManager;}
    public static Y getInstance() {return instance;}

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 初始化
        instance = this;

        foliaLib = new FoliaLib(this);

        pluginStorage = StorageFactory.createStorage(this); // 初始化插件存储

        giftManager = new GiftManager(this); // 初始化礼包管理器

        new CommandManager(this).registerCommands();
        new ListenerManager(this).registerListener();

        // HOOK
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansion(this).register(); //
            LoggerUtils.info(ChatColor.BLUE + "成功挂钩PlaceholderAPI");
        }

        LoggerUtils.info(ChatColor.GREEN + "插件加载成功！");
    }

    @Override
    public void onDisable() {
        // 关闭存储系统
        if (pluginStorage != null) {pluginStorage.shutdown();}

        LoggerUtils.info(ChatColor.RED + "插件卸载成功！");
    }
}
