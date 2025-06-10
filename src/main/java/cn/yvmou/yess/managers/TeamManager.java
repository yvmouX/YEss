package cn.yvmou.yess.managers;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.storage.PlayerDataStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamManager {
    private final Y plugin;
    private final PlayerDataStorage playerDataStorage;

    public TeamManager(Y plugin, PlayerDataStorage playerDataStorage) {
        this.plugin = plugin;
        this.playerDataStorage = playerDataStorage;
    }

    /**
     * 玩家是否被邀请
     * @param player 玩家对象
     * @return true: 已经被邀请, false: 未被邀请
     */
    private boolean isInvited(Player player) {
        return playerDataStorage.isInvited(player);
    }

    /**
     * 玩家是否已经加入一个队伍，会发送信息
     * @param player 玩家对象
     * @return true: 已经加入队伍, false: 未加入队伍
     */
    private boolean senderIsTeamed(Player player) {
        String teamStatus = playerDataStorage.getPlayerData(player);
        if (teamStatus != null) {
            if (isInvited(player)) player.sendMessage("§c你有一个待处理的队伍邀请！");
            else player.sendMessage("§c你已经加入了一个队伍！");
            return true;
        }
        return false;
    }

    /**
     * 玩家是否已经加入一个队伍，会发送信息
     * @param senderPlayer 发送命令的玩家对象
     * @param targetPlayer 被邀请的玩家对象
     * @return true: 已经加入队伍, false: 未加入队伍
     */
    private boolean targetIsTeamed(Player senderPlayer, Player targetPlayer) {
        String teamStatus = playerDataStorage.getPlayerData(targetPlayer);
        if (teamStatus != null) {
            if (isInvited(targetPlayer)) senderPlayer.sendMessage("§c该玩家已经被其他队伍邀请了！");
            else senderPlayer.sendMessage("§c该玩家已经加入了一个队伍！");
            return true;
        }
        return false;
    }
    /**
     * 玩家是否已经加入一个队伍，仅判断
     * @param player 需要判断的玩家对象
     * @return true: 已经加入队伍, false: 未加入队伍
     */
    private boolean hasTeam(Player player) {
        String teamStatus = playerDataStorage.getPlayerData(player);
        if (teamStatus != null && teamStatus.contains("-invited")) return false;
        else return teamStatus != null;
    }

    /**
     * 创建队伍
     * @param senderPlayer 发送命令的玩家对象
     */
    public void createTeam(Player senderPlayer) {
        if (senderIsTeamed(senderPlayer)) return;
        playerDataStorage.setPlayerData(senderPlayer, senderPlayer, false);
    }

    public void inviteTeam(Player senderPlayer, Player targetPlayer) {
        if (targetIsTeamed(senderPlayer, targetPlayer)) return;
        targetPlayer.sendMessage("§a" + senderPlayer + " 邀请你加入他的队伍\n" +
                "输入/yess team accept接收邀请，/yess team deny拒绝邀请");
        playerDataStorage.setPlayerData(senderPlayer, targetPlayer, true);
    }

    public void removePlayer(Player senderPlayer, Player targetPlayer) {
        if (!hasTeam(senderPlayer) &&
                !playerDataStorage.getPlayerData(senderPlayer).equalsIgnoreCase(senderPlayer.getName())) {
            senderPlayer.sendMessage("§c只有队长能够使用该命令");
            return;
        }

        if (!hasTeam(targetPlayer) ||
                !playerDataStorage.getPlayerData(targetPlayer).equalsIgnoreCase(senderPlayer.getName())) {
            senderPlayer.sendMessage("§c无法操作不在自己队伍中的玩家");
            return;
        }

        playerDataStorage.removePlayerData(targetPlayer);
        targetPlayer.sendMessage("§a" + senderPlayer + "将你从队伍中移除");
        senderPlayer.sendMessage("§a你已将" + targetPlayer + "从队伍中移除");
    }

    public void promotePlayer(Player senderPlayer, Player targetPlayer) {
        if (!hasTeam(senderPlayer) &&
                !playerDataStorage.getPlayerData(senderPlayer).equalsIgnoreCase(senderPlayer.getName())) {
            senderPlayer.sendMessage("§c只有队长能够使用该命令");
            return;
        }

        List<Player> members = new ArrayList<>();

        for (Player member : plugin.getServer().getOnlinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(senderPlayer.getName())) {
                members.add(member);
            }
        }

        if (!members.contains(targetPlayer)) {
            senderPlayer.sendMessage("§c该玩家不在线或不在队伍中");
        }

        for (Player member : members) {
            member.sendMessage("§a" + senderPlayer + "将" + targetPlayer + "提升为队长");
            if (member.getName().equalsIgnoreCase(targetPlayer.getName())) member.sendMessage("§a你被" + senderPlayer + "提升为队长");
            playerDataStorage.removePlayerData(member);
            playerDataStorage.setPlayerData(targetPlayer, member, false);
            return;
        }

    }

    public void disbandTeam(Player senderPlayer) {
        if (!hasTeam(senderPlayer) &&
                !playerDataStorage.getPlayerData(senderPlayer).equalsIgnoreCase(senderPlayer.getName())) {
            senderPlayer.sendMessage("§c只有队长能够使用该命令");
            return;
        }

        playerDataStorage.removePlayerData(senderPlayer);

        List<Player> onlineMembers = new ArrayList<>();
        List<OfflinePlayer> offlineMembers = new ArrayList<>();

        for (Player member : plugin.getServer().getOnlinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(senderPlayer.getName())) {
                onlineMembers.add(member);
            }
        }
        for (OfflinePlayer member : plugin.getServer().getOfflinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(senderPlayer.getName())) {
                offlineMembers.add(member);
            }
        }
        for (Player member : onlineMembers) {
            member.sendMessage("§a" + senderPlayer + "已解散你的队伍");
            playerDataStorage.removePlayerData(member);
        }
        for (OfflinePlayer member : offlineMembers) {
            playerDataStorage.removePlayerData(member);
        }
    }

    public void showTeam(Player senderPlayer) {
        Set<String> members = new HashSet<>();

        for (Player member : plugin.getServer().getOnlinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(senderPlayer.getName())) {
                members.add(member.getName());
            }
        }
        for (OfflinePlayer member : plugin.getServer().getOfflinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(senderPlayer.getName())) {
                members.add(member.getName());
            }
        }

        senderPlayer.sendMessage("§a你的队伍成员: ");
        for (String member : members) {
            senderPlayer.sendMessage("§b" + member);
        }

    }

    public void acceptTeam(Player senderPlayer) {
        if (hasTeam(senderPlayer)) {
            senderPlayer.sendMessage("§c你已经加入一个队伍了");
            return;
        } else if (!isInvited(senderPlayer)) {
            senderPlayer.sendMessage("§c你没有被邀请");
            return;
        }

        // 提取队长名称
        String leader_name = playerDataStorage.getPlayerData(senderPlayer).split("-")[0];
        Player leader = plugin.getServer().getPlayer(leader_name);

        // 移除被邀请状态
        playerDataStorage.removePlayerData(senderPlayer);

        for (Player member : plugin.getServer().getOnlinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(leader_name)) {
                member.sendMessage("§a" + senderPlayer + "加入了队伍");
            }
        }

        // 加入队伍
        playerDataStorage.setPlayerData(leader, senderPlayer, false);

    }

    public void denyTeam(Player senderPlayer) {
        if (hasTeam(senderPlayer)) {
            senderPlayer.sendMessage("§c你已经加入一个队伍了");
            return;
        } else if (!isInvited(senderPlayer)) {
            senderPlayer.sendMessage("§c你没有被邀请");
            return;
        }

        // 提取队长名称
        String leader = playerDataStorage.getPlayerData(senderPlayer).split("-")[0];

        // 移除被邀请状态
        playerDataStorage.removePlayerData(senderPlayer);

        senderPlayer.sendMessage("§c你拒绝了" + leader + "的邀请");
        Player leaderPlayer = plugin.getServer().getPlayer(leader);
        if (leaderPlayer != null) leaderPlayer.sendMessage("§c" + senderPlayer + "拒绝了你的邀请");
    }

    public void leaveTeam(Player senderPlayer) {
        if (!hasTeam(senderPlayer)) {
            senderPlayer.sendMessage("§c你没有加入队伍");
            return;
        }

        // 提取队长名称
        String leader = playerDataStorage.getPlayerData(senderPlayer).split("-")[0];

        if (leader.equalsIgnoreCase(senderPlayer.getName())) {
            senderPlayer.sendMessage("§c队长不允许退出队伍，请使用/yess team disband来解散队伍");
            return;
        }

        playerDataStorage.removePlayerData(senderPlayer);

        senderPlayer.sendMessage("§a你退出了队伍");
        for (Player member : plugin.getServer().getOnlinePlayers()) {
            if (playerDataStorage.getPlayerData(member).equalsIgnoreCase(leader)) {
                member.sendMessage("§a" + senderPlayer + "退出了队伍");
            }
        }
    }
}
