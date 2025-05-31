package cn.yvmou.yess.manager;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.main.AliasCommand;
import cn.yvmou.yess.commands.main.MainCommand;
import cn.yvmou.yess.commands.main.MainTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;

public class CommandManager {
    private final YEss plugin;

    public CommandManager(YEss plugin) { this.plugin = plugin; }
    /**
     * 注册所有命令
     */
    public void registerCommands() {
        // 注册主命令
        PluginCommand pc = plugin.getCommand("yess");
        if (pc == null) return;
        pc.setExecutor(new MainCommand(plugin));
        pc.setTabCompleter(new MainTabCompleter(plugin));

        // 注册别名命令
        Runnable r = this::registerAliasCommands;
        if (YEss.getFoliaLib().isFolia()) {
            YEss.getFoliaLib().getScheduler().runNextTick(wrappedTask -> r.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, r);
        }
    }

    private void registerAliasCommands() {
        try {
            // 获取 CommandMap
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

            // 获取 PluginCommand 构造器
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, org.bukkit.plugin.Plugin.class);
            constructor.setAccessible(true);

            // 预先收集所有需要注册的命令
            List<PluginCommand> commandsToRegister = new ArrayList<>();

            // 遍历配置文件中的命令
            ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("RegisterCommand");
            if (configurationSection == null) return;
            Set<String> allCmd = configurationSection.getKeys(false);
            for (String cmd : allCmd) {
                boolean enabled = plugin.getConfig().getBoolean("RegisterCommand." + cmd + ".enable", false);
                String[] allAlias = plugin.getConfig().getString("RegisterCommand." + cmd + ".alias", "none").split(",");

                for (String alias : allAlias) {
                    String a = alias.trim();
                    if (enabled && !alias.equals("none")) {
                        // 创建命令实例
                        PluginCommand pluginCommand = constructor.newInstance(a, plugin);
                        pluginCommand.setExecutor(new AliasCommand(plugin, cmd));
                        pluginCommand.setDescription("YEss plugin command alias for: " + cmd);
                        pluginCommand.setUsage("/" + a);

                        commandsToRegister.add(pluginCommand);
                    }
                }
            }

            // 批量注册所有命令
            for (PluginCommand cmd : commandsToRegister) {
                commandMap.register(plugin.getDescription().getName(), cmd);
                plugin.getLogger().info("已注册别名命令: /" + cmd.getName());
            }

        } catch (Exception e) {
            plugin.getLogger().severe("注册别名命令时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
