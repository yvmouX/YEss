package cn.yvmou.yess.managers;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.gui.GiftEditGUI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiftManager {
    private final YEss plugin;
    private final GiftEditGUI giftEditGUI;
    private final List<ItemStack> items = new ArrayList<>();
    private File giftsFolder;

    public GiftManager(YEss plugin) {
        this.plugin = plugin;
        this.giftEditGUI = new GiftEditGUI();
        this.init();
    }

    public List<ItemStack> getItems() { return items; }
    public GiftEditGUI getGiftEditGUI() { return giftEditGUI; }

    private void init() {
        giftsFolder = new File(plugin.getDataFolder(), "gifts");
        if (!giftsFolder.exists()) {
            giftsFolder.mkdir();
        }
    }

    /**
     *
     * @param name 礼包名称
     * @return 成功返回true，失败反之。
     */
    public boolean createGift(String name) {
        File giftFile = new File(giftsFolder, name + ".yml");
        if (giftFile.exists()) {
            return false;
        }

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.save(giftFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param name 礼包名称
     * @return 该礼包存在返回true，不存在反之。
     */
    public boolean existsGift(String name) {
        return new File(giftsFolder, name + ".yml").exists();
    }

    /**
     *
     * @param name 礼包名称
     */
    public void saveGiftItems(String name) {
        File giftFile = new File(giftsFolder, name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(giftFile);

        config.set("items", null); // 清除旧数据

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            System.out.println(item);

            try {
                config.set("items." + i, item);
                System.out.println("ItemStack 已成功保存到文件: " + giftFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("保存 ItemStack 到文件时出错: " + e.getMessage());
            }

        }

        try {
            config.save(giftFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param name 礼包名称
     * @return ItemStack集合
     */
    public List<ItemStack> loadGiftItems(String name) {
        File giftFile = new File(giftsFolder, name + ".yml");
        if (!giftFile.exists()) {
            return new ArrayList<>();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(giftFile);
        List<ItemStack> items = new ArrayList<>();

        if (config.contains("items")) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                ItemStack itemStack = config.getItemStack("items." + key);
                items.add(itemStack);
            }
        }

        return items;
    }

    /**
     *
     * @param name 礼包名称
     * @return ItemStack
     */
    public ItemStack getGiftItem(String name) {
        File giftFile = new File(giftsFolder, name + ".yml");
        if (!giftFile.exists()) {
            return null;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(giftFile);

        return config.getItemStack("items.0");
    }

    /**
     *
     * @return 所有的礼包
     */
    public List<String> getGiftList() {
        List<String> gifts = new ArrayList<>();
        File[] files = giftsFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                gifts.add(file.getName().replace(".yml", ""));
            }
        }
        return gifts;
    }

} 