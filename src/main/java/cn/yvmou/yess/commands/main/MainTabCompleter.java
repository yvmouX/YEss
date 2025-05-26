package cn.yvmou.yess.commands.main;

import cn.yvmou.yess.YEss;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class MainTabCompleter implements TabCompleter {
    private final YEss plugin;

    public MainTabCompleter(YEss plugin) { this.plugin = plugin; }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
