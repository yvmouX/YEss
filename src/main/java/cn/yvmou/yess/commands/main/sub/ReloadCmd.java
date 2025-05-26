package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return false;
    }
}
