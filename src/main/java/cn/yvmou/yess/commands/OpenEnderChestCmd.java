package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenEnderChestCmd implements SubCommand {
    private final Y plugin;

    public OpenEnderChestCmd(Y plugin) { this.plugin = plugin; }

    @Override
    @CommandOptions(name = "ec", permission = "yess.command.ec", onlyPlayer = true, alias = {"ec"}, register = true, usage = "/yess ec <player>")
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                target.openInventory(target.getEnderChest());
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(player.getEnderChest());
        return true;
    }

}
