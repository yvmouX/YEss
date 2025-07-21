package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCraftCmd implements SubCommand {
    private final Y plugin;

    public OpenCraftCmd(Y plugin) { this.plugin = plugin; }

    @Override
    @CommandOptions(name = "craft", permission = "yess.command.craft", onlyPlayer = true, alias = {"craft"}, register = true, usage = "/yess craft <player>")
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                target.openWorkbench(null, true);
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        Player player = (Player) sender;
        player.openWorkbench(null, true);
        return true;
    }
}
