package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCraftTable implements SubCommand {
    /**
     * 执行子命令逻辑
     *
     * @param sender Command sender
     * @param args   Arguments passed to the command
     * @return true if successful, false otherwise
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                target.openWorkbench(null, true);
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        if (CommandUtils.noPlayer(sender)) return false;

        Player player = (Player) sender;
        player.openWorkbench(null, true);
        return true;
    }

    /**
     * 提供子命令的使用说明
     *
     * @return A string representing command usage
     */
    @Override
    public String getUsage() {
        return "/yess craft";
    }

    /**
     * 检查发送者是否有权限使用子命令.
     *
     * @param sender The command sender
     * @return True if the sender has permission
     */
    @Override
    public String requirePermission(CommandSender sender) {
        return "yess.command.craft";
    }
}
