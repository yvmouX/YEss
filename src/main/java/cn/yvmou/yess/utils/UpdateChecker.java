package cn.yvmou.yess.utils;

import cn.yvmou.yess.Y;
import cn.yvmou.ylib.tools.LoggerTools;
import cn.yvmou.ylib.tools.SchedulerTools;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class UpdateChecker {
    private final Y plugin;
    private final LoggerTools logger = Y.getYLib().getLoggerTools();
    private final SchedulerTools scheduler = Y.getYLib().getSchedulerDogTools();

    public UpdateChecker(Y plugin) {
        this.plugin = plugin;
        checkUpdate();
    }

    private void checkUpdate() {
        scheduler.runTaskAsynchronously(plugin, () -> {
            if (!checkGitHub()) {
                if (!checkSpigot()) {
                    if (!checkModrinth()) {
                        if (!checkMinebbs()) {
                            logger.error("检测更新失败，请联系开发者。");
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
            logger.warn("GitHub更新检查失败" + e);
            return false;
        }
        return true;
    }

    private boolean checkSpigot() {
        try {
            InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=126245").openStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                String latestVersion = scanner.next();
                String currentVersion = plugin.getDescription().getVersion();

                notify(currentVersion, latestVersion, "https://spigotmc.org/resources/126245");
            }
        } catch ( Exception e) {
            logger.warn("Spigot更新检查失败" + e);
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
        scheduler.runTask(plugin, () -> {
            if (!latestVersion.equals(currentVersion)) {
                logger.info("§e==================================");
                logger.info("§a发现新版本: " + latestVersion);
                logger.info("§a当前版本: " + currentVersion);
                logger.info("§a下载: " + Arrays.toString(url));
                logger.info("§e==================================");
            } else {
                logger.info("§e==================================");
                logger.info("§a未发现新版本");
                logger.info("§a当前版本: " + currentVersion);
                logger.info("§a最新版本: " + latestVersion);
                logger.info("§e==================================");
            }
        }, null, null);
    }
}
