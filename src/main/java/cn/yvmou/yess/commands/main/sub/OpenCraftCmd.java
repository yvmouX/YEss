package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCraftCmd implements SubCommand {
    private final Y plugin;

    public OpenCraftCmd(Y plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                target.openWorkbench(null, true);
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        if (CommandUtils.noPlayer(sender)) return false;

        Player player = (Player) sender;
        player.openWorkbench(null, true);
        return true;
    }

    @Override
    public String getUsage() {
        return "/yess craft <player>";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return plugin.getConfig().getString("registerCommand.craft.permission", "yess.command.craft");
    }

    @Override
    public boolean requireRegister() {
        return plugin.getConfig().getBoolean("registerCommand.craft.enable", true);
    }
}
