package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements SubCommand {
    private final YEss plugin;
    public ReloadCmd(YEss plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "成功重载插件！");
        return true;
    }

    @Override
    public String getUsage() {
        return "/yess reload";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return "yess.command.reload";
    }

    @Override
    public Boolean requireRegister() {
        return plugin.getConfig().getBoolean("RegisterCommand.reload.enable");
    }
}
