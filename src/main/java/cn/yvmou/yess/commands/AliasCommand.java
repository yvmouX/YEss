package cn.yvmou.yess.commands;

import cn.yvmou.yess.YEss;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AliasCommand implements CommandExecutor {
    private final YEss plugin;
    private final String originalCommand;

    public AliasCommand(YEss plugin, String originalCommand) {
        this.plugin = plugin;
        this.originalCommand = originalCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = originalCommand;
        System.arraycopy(args, 0, newArgs, 1, args.length);

        return plugin.getServer().dispatchCommand(sender, "yess " + String.join(" ", newArgs));
    }
}