package cn.yvmou.yess.utils.manager;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.AliasCommand;
import cn.yvmou.yess.commands.main.MainCommand;
import cn.yvmou.yess.commands.main.MainTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class CommandManager {
    private final Y plugin;

    public CommandManager(Y plugin) { this.plugin = plugin; }
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
        Y.getYLib().getSchedulerDogTools().runTask(plugin, r, null, null);
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
            ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("registerCommand");
            if (configurationSection == null) return;
            Set<String> allCmd = configurationSection.getKeys(false);
            for (String cmd : allCmd) {
                boolean enabled = plugin.getConfig().getBoolean("registerCommand." + cmd + ".enable", false);
                String[] allAlias = plugin.getConfig().getString("registerCommand." + cmd + ".alias", "none").split(",");

                for (String alias : allAlias) {
                    String a = alias.trim();
                    if (enabled && !alias.equals("none")) {
                        // 创建命令实例
                        PluginCommand pluginCommand = constructor.newInstance(a, plugin);
                        pluginCommand.setExecutor(new AliasCommand(plugin, cmd));
                        pluginCommand.setDescription("Y plugin command alias for: " + cmd);
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