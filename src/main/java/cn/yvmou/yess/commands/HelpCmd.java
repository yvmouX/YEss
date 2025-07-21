package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCmd implements SubCommand {
    private final Y plugin;

    public HelpCmd(Y plugin) { this.plugin = plugin; }

    @Override
    @CommandOptions(name = "help", permission = "yess.command.help", onlyPlayer = false, alias = {}, register = true, usage = "/yess help")
    public boolean execute(CommandSender sender, String[] args) {
        seedHelpMessage(sender);
        return true;
    }

    public void seedHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "===== " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " 帮助 =====");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " help");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " reload");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " craft <player>");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " ec <player>");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " glow <player>");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " gift <create|edit|delete|give|help>");
    }
}
