package cn.yvmou.yess.expansion;

import cn.yvmou.yess.YEss;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PapiExpansion extends PlaceholderExpansion {

    private final YEss plugin;

    public PapiExpansion(YEss plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "yess";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("glow_status")) {
            return YEss.getPluginStorage().isGlowing(player.getUniqueId()) ? "on" : "off";
        }
        return null;
    }
}
