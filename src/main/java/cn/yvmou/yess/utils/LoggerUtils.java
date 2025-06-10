package cn.yvmou.yess.utils;

import cn.yvmou.yess.Y;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LoggerUtils {
    public static final CommandSender console = Bukkit.getConsoleSender();
    private static final String CONSOLE_PREFIX = "§8[" + "§b§l§n" + Y.getInstance().getDescription().getPrefix() + "]§r ";

    public static void info(String msg) {
        info(ChatColor.GREEN, msg); // 默认使用绿色
    }

    public static void info(ChatColor color, String msg) {
        console.sendMessage(CONSOLE_PREFIX + (color == null ? ChatColor.GREEN : color) + msg);
    }

    public static void warn(String msg) {
        warn(ChatColor.GOLD, msg);
    }

    public static void warn(ChatColor color, String msg) {
        console.sendMessage(CONSOLE_PREFIX + (color == null ? ChatColor.GOLD : color) + msg);
    }

    public static void error(String msg) {
        error(ChatColor.RED, msg);
    }

    public static void error(ChatColor color, String msg) {
        console.sendMessage(CONSOLE_PREFIX + (color == null ? ChatColor.RED : color) + msg);
    }

}