package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.YEss;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    private final YEss plugin;

    public MainCommand(YEss plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
