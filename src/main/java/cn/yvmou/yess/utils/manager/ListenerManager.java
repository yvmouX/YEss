package cn.yvmou.yess.utils.manager;

import cn.yvmou.yess.Y;
import cn.yvmou.yess.events.DamageListener;
import cn.yvmou.yess.events.GiftGUIListener;
import cn.yvmou.yess.events.GlowListener;
import cn.yvmou.yess.events.PortalListener;

public class ListenerManager {
    private final Y plugin;

    public ListenerManager(Y plugin) { this.plugin = plugin; }

    public void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(new GlowListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PortalListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GiftGUIListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DamageListener(), plugin);
    }
}
