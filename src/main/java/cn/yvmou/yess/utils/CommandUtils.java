package cn.yvmou.yess.utils;

import cn.yvmou.yess.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandUtils {
    public static void throwAllUsageError(CommandSender sender, Map<String, SubCommand> subCommands) {
        sender.sendMessage(ChatColor.RED + "§c用法错误！可用子命令：");
        for (SubCommand subCommand : subCommands.values()) {
            sender.sendMessage(subCommand.getUsage());
        }
    }

    public static boolean noPermission(CommandSender sender, SubCommand subCommand) {
        if (!sender.hasPermission(subCommand.requirePermission(sender))) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用这个命令!");
            return true;
        }
        return false;
    }

    public static boolean noPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "这个命令只能由玩家执行！");
            return true;
        }
        return false;
    }
}
