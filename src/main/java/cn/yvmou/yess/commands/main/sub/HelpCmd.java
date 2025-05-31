package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCmd implements SubCommand {
    private final YEss plugin;

    public HelpCmd(YEss plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        seedHelpMessage(sender);
        return true;
    }

    @Override
    public String getUsage() {
        return "/yess help";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return "yess.command.help";
    }

    @Override
    public Boolean requireRegister() {
        return plugin.getConfig().getBoolean("RegisterCommand.help.enable");
    }

    public void seedHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "===== " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " 帮助 =====");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " help <player>");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " ec <player>");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " help");
        sender.sendMessage(ChatColor.WHITE + "/" + plugin.getDescription().getName().toLowerCase() + " reload");
    }
}
