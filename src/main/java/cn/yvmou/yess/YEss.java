package cn.yvmou.yess;

import cn.yvmou.yess.manager.CommandManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class YEss extends JavaPlugin {
    private final Logger logger = getLogger();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // 注册命令
        new CommandManager(this).registerCommands();

        logger.info(ChatColor.GREEN + "插件加载成功！");
    }

    @Override
    public void onDisable() {
        logger.info(ChatColor.RED + "插件卸载成功！");
    }

}
