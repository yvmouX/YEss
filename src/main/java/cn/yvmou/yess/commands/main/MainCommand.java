package cn.yvmou.yess.commands.main;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.commands.main.sub.HelpCmd;
import cn.yvmou.yess.commands.main.sub.ReloadCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor {
    private final YEss plugin;
    private Map<String, SubCommand> subCommands = new HashMap<>();

    public MainCommand(YEss plugin) {
        this.plugin = plugin;

        subCommands.put("help", new HelpCmd());
        subCommands.put("reload", new ReloadCmd());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    private void sendVersionMessage(CommandSender sender) {
        sender.sendMessage("YEss version: " + plugin.getDescription().getVersion());
    }


}
