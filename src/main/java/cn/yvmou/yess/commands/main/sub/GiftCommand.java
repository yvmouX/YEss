package cn.yvmou.yess.commands.main.sub;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.commands.SubCommand;
import cn.yvmou.yess.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiftCommand implements SubCommand {
    private final YEss plugin;

    public GiftCommand(YEss plugin) {
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
    public boolean execute(CommandSender sender, String[] args) {
        if (CommandUtils.noPlayer(sender)) return false;
        if (CommandUtils.noPermission(sender, this)) return false;

        Player player = (Player) sender;

        if (args.length < 2) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[1].toLowerCase();
        String giftName = args.length == 3 ? args[2] : "";

        switch (subCommand) {
            case "create" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return true;
                }
                if (YEss.getGiftManager().createGift(giftName)) {
                    player.sendMessage("§a成功创建礼包：" + giftName);
                } else {
                    player.sendMessage("§c礼包已存在！");
                }
            }
            case "edit" -> {
                if (giftName.isEmpty()) {
                    player.sendMessage("§c请输入礼包名称！");
                    return true;
                }
                if (YEss.getGiftManager().existsGift(giftName)) {
                    YEss.getGiftManager().getGiftEditGUI().init(giftName);
                    YEss.getGiftManager().getGiftEditGUI().open(player);
                } else {
                    player.sendMessage("§c礼包不存在！");
                }
            }
            case "list" -> {
                List<String> gifts = YEss.getGiftManager().getGiftList();
                if (gifts.isEmpty()) {
                    player.sendMessage("§c暂无礼包！");
                } else {
                    player.sendMessage("§a礼包列表：");
                    gifts.forEach(gift -> player.sendMessage("§7- " + gift));
                }
            }
            case "give" -> {
                ItemStack itemStack = YEss.getGiftManager().getGiftItem(giftName);
                player.getInventory().addItem(itemStack);
            }
            case "help" -> sendHelp(player);
            default -> sender.sendMessage(getUsage());
        }

        return true;
    }

    /**
     * 提供子命令的使用说明
     *
     * @return A string representing command usage
     */
    @Override
    public String getUsage() {
        return "/yess gift help";
    }

    /**
     * 检查发送者是否有权限使用子命令.
     *
     * @param sender The command sender
     * @return True if the sender has permission
     */
    @Override
    public String requirePermission(CommandSender sender) {
        return plugin.getConfig().getString("RegisterCommand.gift.permission");
    }

    /**
     * 是否注册命令
     *
     * @return 注册命令返回是，否则否
     */
    @Override
    public Boolean requireRegister() {
        return plugin.getConfig().getBoolean("RegisterCommand.gift.enable");
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6=== 礼包命令帮助 ===");
        player.sendMessage("§f/yess gift create <名称> §7- 创建礼包");
        player.sendMessage("§f/yess gift edit <名称> §7- 编辑礼包");
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
//            completions.addAll(YEss.getGiftManager().getGiftList());
//        }
//
//        return completions;
//    }
} 