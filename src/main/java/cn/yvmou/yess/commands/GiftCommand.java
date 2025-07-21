package cn.yvmou.yess.commands;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.api.command.CommandOptions;
import cn.yvmou.ylib.api.command.SubCommand;
import cn.yvmou.ylib.tools.MessageTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiftCommand implements SubCommand {
    private final Y plugin;
    private final MessageTools message = Y.getYLib().getMessageTools();

    public GiftCommand(Y plugin) {
        this.plugin = plugin;
    }

    /**
     * 执行子命令逻辑
     *
     * @param sender Command sender
     * @param args   Arguments passed to the command
     * @return true if successful, false otherwise
     */
    @Override
    @CommandOptions(name = "gift", permission = "yess.command.gift", onlyPlayer = true, alias = {"gift"}, register = true, usage = "/yess gift help")
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            sendHelp(player);
            return false;
        }

        String subCommand = args[1].toLowerCase();
        String giftName = args.length >= 3 ? args[2] : "";

        switch (subCommand) {
            case "create" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                if (Y.getGiftM().createGift(giftName)) {
                    player.sendMessage("§a成功创建礼包：" + giftName);
                } else {
                    player.sendMessage("§c礼包已存在！");
                }
            }
            case "edit" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                if (Y.getGiftM().existsGift(giftName)) {
                    Y.getGiftM().getGiftEditGUI().init(giftName);
                    Y.getGiftM().getGiftEditGUI().open(player);
                } else {
                    player.sendMessage("§c礼包不存在！");
                }
            }
            case "list" -> {
                List<String> gifts = Y.getGiftM().getGiftList();
                if (gifts.isEmpty()) {
                    player.sendMessage("§c暂无礼包！");
                } else {
                    player.sendMessage("§a礼包列表：");
                    gifts.forEach(gift -> player.sendMessage("§7- " + gift));
                }
            }
            case "get" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                for (ItemStack item : Y.getGiftM().getGiftItem(player, giftName)) {
                    player.getInventory().addItem(item);
                }
            }
            case "give" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                if (args.length < 4) {
                    player.sendMessage("§c用法错误：/yess gift give <礼包名称> <玩家>");
                    return false;
                }
                Player target = Bukkit.getPlayerExact(args[3]);
                if (target != null) {
                    List<ItemStack> items = Y.getGiftM().getGiftItem(player, giftName);
                    if (items == null) {
                        message.error(player, "似乎没有这个礼包");
                    } else {
                        for (ItemStack item : items) {
                            target.getInventory().addItem(item);
                        }
                    }
                } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            }
            case "look" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                if (args.length < 4) {
                    player.sendMessage("§c用法错误：/yess gift give <礼包名称> <玩家>");
                    return false;
                }
                Player target = Bukkit.getPlayerExact(args[3]);
                if (target != null) {
                    Y.getGiftM().getGiftLookGUI().init(giftName);
                    Y.getGiftM().getGiftLookGUI().open(target);
                } else sender.sendMessage(ChatColor.RED + "这个玩家不在线");
            }
            case "delete" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return false;
                }
                Y.getGiftM().deleteGift(giftName, player);
            }
            case "help" -> sendHelp(player);
            default -> sender.sendMessage(Y.getYLib().getCommandConfig().getUsage("yess", "gift"));
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6=== 礼包命令帮助 ===");
        player.sendMessage("§f/yess gift create <名称> §7- 创建礼包");
        player.sendMessage("§f/yess gift edit <名称> §7- 编辑礼包");
        player.sendMessage("§f/yess gift get <名称> §7- 获取礼包");
        player.sendMessage("§f/yess gift give <名称> <玩家> §7- 给予玩家礼包");
        player.sendMessage("§f/yess gift look <名称> <玩家> §7- 预览");
        player.sendMessage("§f/yess gift delete <名称> §7- 预览");
        player.sendMessage("§f/yess gift list §7- 查看礼包列表");
    }

//    @Override
//    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
//        List<String> completions = new ArrayList<>();
//
//        if (args.length == 1) {
//            completions.add("create");
//            completions.add("edit");
//            completions.add("list");
//        } else if (args.length == 2 && (args[0].equalsIgnoreCase("edit"))) {
//            completions.addAll(Y.getGiftM().getGiftList());
//        }
//
//        return completions;
//    }
} 