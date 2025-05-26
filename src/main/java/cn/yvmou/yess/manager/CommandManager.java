package cn.yvmou.yess.manager;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.main.sub.MainCommand;
import cn.yvmou.yess.commands.main.MainTabCompleter;

import java.util.Objects;

public class CommandManager {
    private final YEss plugin;

    public CommandManager(YEss plugin) { this.plugin = plugin; }
    /**
     * 注册所有命令
     */
    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("yess")).setExecutor(new MainCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("yess")).setTabCompleter(new MainTabCompleter(plugin));
    }
}
