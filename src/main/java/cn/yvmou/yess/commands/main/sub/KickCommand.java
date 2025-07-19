package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements SubCommand {
    private final Y plugin;
    Player target;
    String reason;

    public KickCommand(Y plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(getUsage());
            return false;
        }

        if (args.length == 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            }
            return true;
        }

        if (args.length == 3) {
            target = Bukkit.getPlayerExact(args[1]);
            reason = args[2];
            if (target != null) {
                target.kickPlayer(reason);
            }
        }

        return true;
    }

    @Override
    public String getUsage() {
        return "/yess kick <player>";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return plugin.getConfig().getString("registerCommand.kick.permission", "yess.command.kick");
    }

    @Override
    public boolean requireRegister() {
        return plugin.getConfig().getBoolean("registerCommand.kick.enable", true);
    }
}
