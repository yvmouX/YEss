package cn.yvmou.yess.utils;

import cn.yvmou.yess.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandUtils {
    /**
     * 抛出用法错误
     * @param sender
     * @param subCommands
     */
    public static void throwUsageError(CommandSender sender, Map<String, SubCommand> subCommands) {
        sender.sendMessage(ChatColor.RED + "§c用法错误！可用子命令：");
        for (SubCommand subCommand : subCommands.values()) {
            sender.sendMessage(subCommand.getUsage());
        }

    }

    /**
     * 抛出权限错误
     * @param sender
     */
    public static void throwPermissionError(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "你没有权限使用这个命令!");
    }
}
