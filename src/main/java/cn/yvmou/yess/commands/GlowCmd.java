package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
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
    @CommandOptions(name = "glow", permission = "yess.command.glow", onlyPlayer = true, alias = {"glow"}, register = true, usage = "/yess glow <player>")
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                glowToggle(target);
            } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            return true;
        }

        glowToggle((Player) sender);
        return true;
    }

    private void glowToggle(Player player) {
        UUID uuid = player.getUniqueId();
        boolean currentlyGlowing = Y.glowM().getIsGlowing(uuid);

        if (currentlyGlowing) {
            player.removePotionEffect(PotionEffectType.GLOWING);
            Y.glowM().setIsGlowing(uuid, false);
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
            Y.glowM().setIsGlowing(uuid, true);
            player.sendMessage(ChatColor.RED + "你获得了发光效果");
        }
    }
}
