package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;

public class BanCommand implements SubCommand {
    private final Y plugin;
    String playerName;
    long banTimeInMillis;

    public BanCommand(Y plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(getUsage());
            return false;
        }

        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                playerName = target.getName();
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        if (args.length == 4) {
            String expires = args[3];
            try {
                if (expires.endsWith("d")) {
                    banTimeInMillis = Integer.parseInt(expires.toLowerCase().replace("d", "")) * 24 * 60 * 60 * 1000L;
                } else if (expires.endsWith("h")) {
                    banTimeInMillis = Integer.parseInt(expires.toLowerCase().replace("h", "")) * 60 * 60 * 1000L;
                } else if (expires.endsWith("m")) {
                    banTimeInMillis = Integer.parseInt(expires.toLowerCase().replace("m", "")) * 60 * 1000L;
                } else if (expires.endsWith("s")) {
                    banTimeInMillis = Integer.parseInt(expires.toLowerCase().replace("s", "")) * 1000L;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(getUsage());
                player.sendMessage("§c错误：封禁时间格式不正确！示例: 1d, 2h, 30m, 5000");
            }

        }

        Player p = Bukkit.getPlayer(playerName);
        if (p != null) {
            p.ban(args[2], new Date(System.currentTimeMillis() - banTimeInMillis), null, true);
        }

        player.ban(args[1], Instant.parse(args[2]), null, true);
        return true;
    }

    @Override
    public String getUsage() {
        return "/yess ban <player> <reason> <expires>";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return plugin.getConfig().getString("registerCommand.ban.permission", "yess.command.ban");
    }

    @Override
    public boolean requireRegister() {
        return plugin.getConfig().getBoolean("registerCommand.ban.enable", true);
    }
}
