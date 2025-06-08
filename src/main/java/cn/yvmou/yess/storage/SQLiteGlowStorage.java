package cn.yvmou.yess.storage;

import cn.yvmou.yess.YEss;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteGlowStorage implements PluginStorage {
    private final YEss plugin;
    private Connection connection;

    public SQLiteGlowStorage(YEss plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/glow_data.db");
            createTableIfNotExists();
            plugin.getLogger().info("SQLite 存储已初始化");
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "无法初始化SQLite存储", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "关闭SQLite连接时出错", e);
        }
    }

    @Override
    public void setGlowing(UUID playerId, boolean glowing) {
        if (glowing) {
            // 使用INSERT OR REPLACE确保唯一性
            String sql = "INSERT OR REPLACE INTO player_glow_status (uuid, glowing) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, playerId.toString());
                stmt.setBoolean(2, true);
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "保存发光状态失败", e);
            }
        } else {
            String sql = "DELETE FROM player_glow_status WHERE uuid = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, playerId.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "删除发光状态失败", e);
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
            plugin.getLogger().log(Level.SEVERE, "查询发光状态失败", e);
            return false;
        }
    }

    @Override
    public Set<UUID> getAllGlowingPlayers() {
        Set<UUID> glowingPlayers = new HashSet<>();
        String sql = "SELECT uuid FROM player_glow_status WHERE glowing = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                glowingPlayers.add(UUID.fromString(rs.getString("uuid")));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "获取所有发光玩家失败", e);
        }
        return glowingPlayers;
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM player_glow_status";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "清空发光状态数据失败", e);
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS player_glow_status (" +
                "uuid TEXT PRIMARY KEY, " +
                "glowing INTEGER NOT NULL, " +  // SQLite使用INTEGER表示布尔值
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    /**
     * 执行数据库迁移（如果需要）
     */
    public void migrateIfNeeded() {
        // 检查表结构是否是最新的
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet columns = meta.getColumns(null, null, "player_glow_status", "last_updated");
            if (!columns.next()) {
                // 添加last_updated列
                String sql = "ALTER TABLE player_glow_status ADD COLUMN last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "数据库迁移检查失败", e);
        }
    }
}

