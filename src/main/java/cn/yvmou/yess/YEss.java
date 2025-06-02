package cn.yvmou.yess;

import cn.yvmou.yess.events.PlayerListener;
import cn.yvmou.yess.expansion.PapiExpansion;
import cn.yvmou.yess.manager.CommandManager;
import cn.yvmou.yess.storage.GlowStorage;
import cn.yvmou.yess.storage.StorageFactory;
import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class YEss extends JavaPlugin {
    private final Logger logger = getLogger();
    private static FoliaLib foliaLib;
    private GlowStorage glowStorage;

    public static FoliaLib getFoliaLib() {return foliaLib;}
    public GlowStorage getGlowStorage() {
        return glowStorage;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // 初始化
        foliaLib = new FoliaLib(this);
        glowStorage = StorageFactory.createStorage(this);
        glowStorage.init();
        // 注册命令
        new CommandManager(this).registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        // HOOK
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new PapiExpansion(this).register(); //
            logger.info(ChatColor.BLUE + "成功挂钩PlaceholderAPI");
        }

        logger.info(ChatColor.GREEN + "插件加载成功！");
    }

    @Override
    public void onDisable() {
        // 关闭存储系统
        if (glowStorage != null) {glowStorage.shutdown();}

        logger.info(ChatColor.RED + "插件卸载成功！");
    }

}
