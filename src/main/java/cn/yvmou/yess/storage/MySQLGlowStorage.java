package cn.yvmou.yess.storage;

import cn.yvmou.yess.Y;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MySQLGlowStorage implements PluginStorage {
    private final Y plugin;
    private Connection connection;

    public MySQLGlowStorage(Y plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        String host = plugin.getConfig().getString("mysql.host");
        int port = plugin.getConfig().getInt("mysql.port");
        String database = plugin.getConfig().getString("mysql.database");
        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";

        try {
            connection = DriverManager.getConnection(url, username, password);
            createTableIfNotExists();
        } catch (SQLException e) {
            plugin.getLogger().severe("无法连接到MySQL数据库: " + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().warning("关闭MySQL连接时出错: " + e.getMessage());
            }
        }
    }

    @Override
    public void setGlowing(UUID playerId, boolean glowing) {
        if (glowing) {
            // 使用REPLACE INTO确保唯一性
            String sql = "REPLACE INTO player_glow_status (uuid, glowing) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, playerId.toString());
                stmt.setBoolean(2, true);
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("保存发光状态失败: " + e.getMessage());
            }
        } else {
            String sql = "DELETE FROM player_glow_status WHERE uuid = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, playerId.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("删除发光状态失败: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isGlowing(UUID playerId) {
        String sql = "SELECT glowing FROM player_glow_status WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerId.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean("glowing");
        } catch (SQLException e) {
            plugin.getLogger().severe("查询发光状态失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Set<UUID> getAllGlowingPlayers() {
        Set<UUID> glowingPlayers = new HashSet<>();
        String sql = "SELECT uuid FROM player_glow_status WHERE glowing = true";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                glowingPlayers.add(UUID.fromString(rs.getString("uuid")));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("获取所有发光玩家失败: " + e.getMessage());
        }
        return glowingPlayers;
    }

    @Override
    public void clearAll() {
        String sql = "TRUNCATE TABLE player_glow_status";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("清空发光状态数据失败: " + e.getMessage());
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS player_glow_status (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "glowing BOOLEAN NOT NULL, " +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}