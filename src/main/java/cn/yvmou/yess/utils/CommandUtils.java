package cn.yvmou.yess.utils;

import cn.yvmou.yess.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.util.Map;

public class CommandUtils {
    /**
     * 抛出所有命令的用法错误消息
     * @param sender
     * @param subCommands
     */
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

//    public static void sendCommandHelpList(CommandSender sender, Map<String, SubCommand> subCommands, JavaPlugin plugin) {
//        sender.sendMessage(ChatColor.GREEN + "===== " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " 帮助 =====");
//        for (SubCommand subCommand : subCommands.values()) {
//            sender.sendMessage(ChatColor.YELLOW + subCommand.getUsage());
//            //sender.sendMessage(ChatColor.YELLOW + "/" + subCommand.getUsage() + " - " + subCommand.getDescription());
//        }
//
//    };
}
