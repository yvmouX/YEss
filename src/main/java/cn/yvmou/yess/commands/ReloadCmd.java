package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements SubCommand {
    private final Y plugin;
    public ReloadCmd(Y plugin) { this.plugin = plugin; }

    @Override
    @CommandOptions(name = "reload", permission = "yess.command.reload", onlyPlayer = true, alias = {}, register = true, usage = "/yess reload")
    public boolean execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "成功重载插件！");
        return true;
    }
}
