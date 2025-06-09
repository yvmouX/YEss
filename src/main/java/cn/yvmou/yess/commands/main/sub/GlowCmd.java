package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GlowCmd implements SubCommand {
    private final Y plugin;

    public GlowCmd(Y plugin) { this.plugin = plugin; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPermission(sender, this)) return false;

        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                glowToggle(target);
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        if (CommandUtils.noPlayer(sender)) return false;

        glowToggle((Player) sender);
        return true;
    }

    @Override
    public String getUsage() {
        return "/yess glow <player>";
    }

    @Override
    public String requirePermission(CommandSender sender) {
        return plugin.getConfig().getString("registerCommand.reload.permission", "yess.command.reload");
    }

    @Override
    public Boolean requireRegister() {
        return plugin.getConfig().getBoolean("registerCommand.glow.enable", true);
    }

    private void glowToggle(Player player) {
        UUID playerId = player.getUniqueId();
        boolean currentlyGlowing = Y.getPluginStorage().isGlowing(playerId);

        if (currentlyGlowing) {
            player.removePotionEffect(PotionEffectType.GLOWING);
            Y.getPluginStorage().setGlowing(playerId, false);
            player.sendMessage(ChatColor.RED + "发光效果已移除!");
        } else {
            player.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.GLOWING,
                            Integer.MAX_VALUE,
                            0,
                            false,
                            false,
                            true
                    )
            );
            Y.getPluginStorage().setGlowing(playerId, true);
            player.sendMessage(ChatColor.RED + "你获得了发光效果");
        }
    }
}
