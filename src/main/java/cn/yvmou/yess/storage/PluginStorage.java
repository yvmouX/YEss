package cn.yvmou.yess.storage;

import java.util.Set;
import java.util.UUID;

public interface PluginStorage {
    /**
     * 初始化存储系统
     */
    void init();

    /**
     * 关闭存储系统
     */
    void shutdown();

    /**
     * 设置玩家发光状态
     */
    void setGlowing(UUID playerId, boolean glowing);

    /**
     * 检查玩家是否发光
     */
    boolean isGlowing(UUID playerId);

    /**
     * 获取所有发光玩家的UUID
     */
    Set<UUID> getAllGlowingPlayers();

    /**
     * 清除所有数据
     */
    void clearAll();
}

