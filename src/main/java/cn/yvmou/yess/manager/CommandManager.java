package cn.yvmou.yess.manager;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.main.AliasCommand;
import cn.yvmou.yess.commands.main.MainCommand;
import cn.yvmou.yess.commands.main.MainTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class CommandManager {
    private final YEss plugin;

    public CommandManager(YEss plugin) { this.plugin = plugin; }
    /**
     * 注册所有命令
     */
    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("yess")).setExecutor(new MainCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("yess")).setTabCompleter(new MainTabCompleter(plugin));

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
            for (String cmd : plugin.getConfig().getConfigurationSection("RegisterCommand").getKeys(false)) {
                String alias = plugin.getConfig().getString("RegisterCommand." + cmd + ".alias");
                boolean enabled = plugin.getConfig().getBoolean("RegisterCommand." + cmd + ".enable", false);

                if (enabled && alias != null && !alias.equals("none")) {
                    // 创建命令实例
                    PluginCommand pluginCommand = constructor.newInstance(alias, plugin);
                    pluginCommand.setExecutor(new AliasCommand(plugin, cmd));
                    pluginCommand.setDescription("YEss plugin command alias for: " + cmd);
                    pluginCommand.setUsage("/" + alias);

                    commandsToRegister.add(pluginCommand);
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

//    private void unregisterOldCommands() {
//        try {
//            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
//            commandMapField.setAccessible(true);
//            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());
//
//            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
//            knownCommandsField.setAccessible(true);
//            @SuppressWarnings("unchecked")
//            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
//
//            // 移除插件之前注册的命令
//            knownCommands.entrySet().removeIf(entry -> {
//                Command cmd = entry.getValue();
//                return cmd instanceof PluginCommand && ((PluginCommand) cmd).getPlugin() == plugin;
//            });
//        } catch (Exception e) {
//            plugin.getLogger().severe("注销旧命令时发生错误: " + e.getMessage());
//        }
//    }
//
}
