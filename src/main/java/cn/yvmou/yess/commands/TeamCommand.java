package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.managers.TeamM;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements SubCommand {
    private final Y plugin;
    private final TeamM teamM = Y.getTeamM();

    public TeamCommand(Y plugin) {
        this.plugin = plugin;
    }

    /**
     * 执行子命令逻辑
     *
     * @param sender Command sender
     * @param args Arguments passed to the command
     * @return true if successful, false otherwise
     */
    @Override
    @CommandOptions(name = "team", permission = "yess.command.team", onlyPlayer = true, alias = {"team"}, register = true, usage = "/yess team help")
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            sendHelp(player);
            return false;
        }

        String subCommand = args[1].toLowerCase();
        String targetName = args.length > 2 ? args[2] : "";

        switch (subCommand) {
            case "create" -> teamM.createTeam(player);
            case "invite" -> {
                if (targetName.isEmpty()) {
                    player.sendMessage("§c请输入玩家名称！");
                    return false;
                }
                Player target = plugin.getServer().getPlayerExact(targetName);
                if (target != null) {
                    teamM.inviteTeam(player, target);
                } else {
                    player.sendMessage("§c该玩家不在线！");
                }
            }
            case "remove" -> {
                if (targetName.isEmpty()) {
                    player.sendMessage("§c请输入玩家名称！");
                    return false;
                }
                Player target = plugin.getServer().getPlayerExact(targetName);
                if (target != null) {
                    teamM.removePlayer(player, target);
                } else {
                    player.sendMessage("§c该玩家不在线！");
                }
            }
            case "promote" -> {
                if (targetName.isEmpty()) {
                    player.sendMessage("§c请输入玩家名称！");
                    return false;
                }
                Player target = plugin.getServer().getPlayerExact(targetName);
                if (target != null) {
                    teamM.promotePlayer(player, target);
                } else {
                    player.sendMessage("§c该玩家不在线！");
                }
            }
            case "disband" -> teamM.disbandTeam(player);
            case "show" -> teamM.showTeam(player);
            case "leave" -> teamM.leaveTeam(player);
            case "accept" -> teamM.acceptTeam(player);
            case "deny" -> teamM.denyTeam(player);
            case "help" -> sendHelp(player);
            default -> player.sendMessage(Y.getYLib().getCommandConfig().getUsage("yess", "team"));
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6=== 队伍命令帮助 ===");
        player.sendMessage("§a--- 队长命令 ---");
        player.sendMessage("§f/yess team create §7- 创建队伍");
        player.sendMessage("§f/yess team invite <玩家> §7- 邀请玩家加入队伍");
        player.sendMessage("§f/yess team remove <玩家> §7- 将玩家移出队伍");
        player.sendMessage("§f/yess team promote <玩家> §7- 将队长转让给玩家");
        player.sendMessage("§f/yess team disband §7- 解散队伍");
        player.sendMessage("§b--- 成员命令 ---");
        player.sendMessage("§f/yess team show §7- 预览当前队伍");
        player.sendMessage("§f/yess team leave §7- 离开当前队伍");
        player.sendMessage("§f/yess team accept §7- 同意加入队伍");
        player.sendMessage("§f/yess team deny §7- 拒绝加入队伍");
    }
}
