package cn.yvmou.yess.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUIUtils {
    /**
     *
     * @param material 物品material
     * @param name 物品名称
     * @param lore 物品lore，可多行
     * @return 返回ItemStack
     */
    public static ItemStack createButton(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> loreList = new ArrayList<>();
            Collections.addAll(loreList, lore);
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }

}
