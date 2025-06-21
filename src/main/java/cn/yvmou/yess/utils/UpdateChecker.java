package cn.yvmou.yess.utils;

import cn.yvmou.yess.Y;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class UpdateChecker {
    private final Y plugin;

    public UpdateChecker(Y plugin) {
        this.plugin = plugin;
        checkUpdate();
    }

    private void checkUpdate() {
        SchedulerUtils.runTaskAsynchronously(plugin, () -> {
            if (!checkGitHub()) {
                if (!checkSpigot()) {
                    if (!checkModrinth()) {
                        if (!checkMinebbs()) {
                            LoggerUtils.error("检测更新失败，请联系开发者。");
                        }
                    }
                }
            }
        });
    }

    private boolean checkGitHub() {
        try {
            URL url = new URL("https://api.github.com/repos/yvmouX/YEss/releases/latest");
            // 读取GitHub Release的JSON数据
            String json = new BufferedReader(new InputStreamReader(url.openStream())).lines().reduce((a, b) -> a + b).orElse("{}");

            // 解析JSON并获取name
            String tag_name = new JSONObject(json).getString("tag_name");

            String latestVersion = tag_name.replace("v", "");
            String currentVersion = plugin.getDescription().getVersion();

            notify(currentVersion, latestVersion, "https://github.com/yvmouX/YEss/releases/latest");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.warn("GitHub更新检查失败");
            return false;
        }
        return true;
    }

    private boolean checkSpigot() {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/" + 126245 + "/versions/latest");

            String json = new BufferedReader(new InputStreamReader(url.openStream())).lines().reduce((a, b) -> a + b).orElse("{}");

            String latestVersion = new JSONObject(json).getString("name");
            String currentVersion = plugin.getDescription().getVersion();
            System.out.println(latestVersion);
            System.out.println(currentVersion);

            notify(currentVersion, latestVersion, "https://spigotmc.org/resources/126245");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.warn("Spigot更新检查失败");
            return false;
        }
        return true;
    }

    private boolean checkModrinth() {
        return true;
    }

    private boolean checkMinebbs() {
        return true;
    }

    private void notify(String currentVersion, String latestVersion, String... url) {
        SchedulerUtils.runTask(plugin, () -> {
            if (!latestVersion.equals(currentVersion)) {
                LoggerUtils.info("§e==================================");
                LoggerUtils.info("§a发现新版本: " + latestVersion);
                LoggerUtils.info("§a当前版本: " + currentVersion);
                LoggerUtils.info("§a下载: " + Arrays.toString(url));
                LoggerUtils.info("§e==================================");
            } else {
                LoggerUtils.info("§e==================================");
                LoggerUtils.info("§a未发现新版本");
                LoggerUtils.info("§a当前版本: " + currentVersion);
                LoggerUtils.info("§a最新版本: " + latestVersion);
                LoggerUtils.info("§e==================================");
            }
        });
    }
}
