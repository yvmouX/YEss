package cn.yvmou.yess;

import cn.yvmou.yess.commands.*;
import cn.yvmou.yess.managers.GiftM;
import cn.yvmou.yess.expansion.PapiExpansion;
import cn.yvmou.yess.managers.TeamM;
import cn.yvmou.yess.managers.GlowM;
import cn.yvmou.yess.storage.Storage;
import cn.yvmou.yess.utils.Metrics;
import cn.yvmou.yess.utils.UpdateChecker;
import cn.yvmou.yess.storage.StorageType;
import cn.yvmou.yess.utils.manager.ListenerManager;
import cn.yvmou.ylib.YLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Y extends JavaPlugin {
    private static YLib yLib;
    private static GlowM glowM;
    private static Storage storage;
    private static GiftM giftM;
    private static TeamM teamM;

    public static YLib getYLib() { return yLib; }
    public static Storage getStorage() {return storage;}
    public static GlowM glowM() { return glowM; }
    public static GiftM getGiftM() {return giftM;}
    public static TeamM getTeamM() {return teamM;}

    @Override
    public void onEnable() {
        yLib = new YLib(this);

        storage = StorageType.createStorage(this); // 初始化插件存储

        giftM = new GiftM(this); // 初始化礼包管理器
        teamM = new TeamM(this, storage);
        glowM = new GlowM(storage);

        getYLib().getCommandManager().registerCommands(
                "yess",
                new BanCommand(this),
                new GiftCommand(this),
                new GlowCmd(this),
                new KickCommand(this),
                new OpenCraftCmd(this),
                new OpenEnderChestCmd(this),
                new ReloadCmd(this),
                new TeamCommand(this)
        );
        new ListenerManager(this).registerListener();

        // HOOK
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansion(this).register(); //
            getYLib().getLoggerTools().info(ChatColor.BLUE + "成功挂钩PlaceholderAPI");
        }
        // 检测配置文件是否更新
        getYLib().getConfigTools().checkConfigVersion();
        // 插件统计
        new UpdateChecker(this);
        // 检测插件更新
        Metrics metrics = new Metrics(this, 26229);
        getYLib().getLoggerTools().info(ChatColor.GREEN + "插件加载成功！");
    }

    @Override
    public void onDisable() {
        // 关闭存储系统
        if (storage != null) {storage.close();}

        getYLib().getLoggerTools().info(ChatColor.RED + "插件卸载成功！");
    }

}