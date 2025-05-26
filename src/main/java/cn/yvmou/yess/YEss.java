package cn.yvmou.yess;

import cn.yvmou.yess.manager.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class YEss extends JavaPlugin {

    @Override
    public void onEnable() {
        // 注册命令和事件
        new CommandManager(this).registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
