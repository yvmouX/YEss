package cn.yvmou.yess.commands.main;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.commands.main.sub.HelpCmd;
import cn.yvmou.yess.commands.main.sub.OpenCraftTable;
import cn.yvmou.yess.commands.main.sub.OpenEnderChestCmd;
import cn.yvmou.yess.commands.main.sub.ReloadCmd;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor {
    private final YEss plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public MainCommand(YEss plugin) {
        this.plugin = plugin;

        subCommands.put("help", new HelpCmd(plugin));
        subCommands.put("reload", new ReloadCmd(plugin));
        subCommands.put("ec", new OpenEnderChestCmd());
        subCommands.put("craft", new OpenCraftTable());
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendVersionMessage(sender);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if (subCommand == null) {
            CommandUtils.throwAllUsageError(sender, subCommands);
            return true;
        }

        return subCommand.execute(sender, args);
    }

    private void sendVersionMessage(CommandSender sender) {
        sender.sendMessage(plugin.getDescription().getName() + " version: " + plugin.getDescription().getVersion());
    }
}
