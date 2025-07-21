package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
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
    @CommandOptions(name = "kick", permission = "yess.command.kick", onlyPlayer = true, alias = {"kick"}, register = true, usage = "/yess kick <player>")
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Y.getYLib().getCommandConfig().getUsage("yess", "kick"));
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
}
