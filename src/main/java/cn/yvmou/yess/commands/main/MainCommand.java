package cn.yvmou.yess.commands.main;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.commands.main.sub.HelpCmd;
import cn.yvmou.yess.commands.main.sub.ReloadCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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



    private void sendVersionMessage(CommandSender sender) {
        sender.sendMessage("YEss version: " + plugin.getDescription().getVersion());
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
