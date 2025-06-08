package cn.yvmou.yess.utils.manager;

import cn.yvmou.yess.YEss;
import cn.yvmou.yess.events.GiftGUIListener;
import cn.yvmou.yess.events.GlowListener;
import cn.yvmou.yess.events.PortalListener;

public class ListenerManager {
    private final YEss plugin;

    public ListenerManager(YEss plugin) { this.plugin = plugin; }

    public void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(new GlowListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PortalListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GiftGUIListener(), plugin);
    }
}
