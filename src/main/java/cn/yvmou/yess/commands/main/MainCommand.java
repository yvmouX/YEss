package cn.yvmou.yess.commands.main;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.commands.main.sub.*;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainCommand implements CommandExecutor {
    private final Y plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>(); // 子命令 SubCommand

    public MainCommand(Y plugin) {
        this.plugin = plugin;

        subCommands.put("help", new HelpCmd(plugin));
        subCommands.put("reload", new ReloadCmd(plugin));
        subCommands.put("ec", new OpenEnderChestCmd(plugin));
        subCommands.put("craft", new OpenCraftCmd(plugin));
        subCommands.put("glow", new GlowCmd(plugin));
        subCommands.put("gift", new GiftCommand(plugin));
        subCommands.put("team", new TeamCommand(plugin));

        // 迭代器
        // 移除未注册命令
        Iterator<Map.Entry<String, SubCommand>> it = subCommands.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, SubCommand> entry = it.next();
            if (!entry.getValue().requireRegister()) {
                it.remove();
                plugin.getLogger().info("已移除未注册命令：" + entry.getKey());
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendVersionMessage(sender);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if (subCommand == null) {
            CommandUtils.throwAllUsageError(sender, subCommands);
            return true;
        }

        return subCommand.execute(sender, args);
    }

    private void sendVersionMessage(CommandSender sender) {
        sender.sendMessage(plugin.getDescription().getName() + " version: " + plugin.getDescription().getVersion());
    }
}
