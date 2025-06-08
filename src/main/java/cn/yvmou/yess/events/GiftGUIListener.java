package cn.yvmou.yess.events;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.gui.GiftEditGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiftGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String title = event.getView().getTitle();
        if (title.startsWith("§6编辑礼包: ")) {
            // 只允许在前45格放置物品
            if (event.getRawSlot() >= 45 && event.getRawSlot() <= 53) {
                event.setCancelled(true);

                handleClick(player, event.getSlot());
            }
        }
    }

    /**
     * 处理按钮的点击
     *
     * @param player 玩家
     * @param slot 位置
     */
    public void handleClick(Player player, int slot) {
        if (slot == 49) { // 保存按钮
            for (int i = 0; i < 45; i++) {
                ItemStack item = YEss.getGiftManager().getGiftEditGUI().getInventory().getItem(i);

                if (item != null) {
                    YEss.getGiftManager().getItems().add(item);
                }
            }

            YEss.getGiftManager().saveGiftItems(YEss.getGiftManager().getGiftEditGUI().getGiftName());

            player.closeInventory();
        } else if (slot == 45) { // 取消按钮
            player.closeInventory();
        } else if (slot == 53) { // 清空按钮
            for (int i = 0; i < 45; i++) {
                YEss.getGiftManager().getGiftEditGUI().getInventory().setItem(i, null);
            }
            player.sendMessage("§c礼包已清空！");
        }
    }
}
