# YEss
基础功能插件

## CommandHelp

===== YEss v1.0.0 帮助 =====

- /yess help <player>
  - yess.command.help
- /yess ec <player>
  - yess.command.ec
- /yess glow <player>
  - yess.command.glow

- /yess help
  - yess.command.help
- /yess reload
  - yess.command.reload

## Config.yml

```yaml
Version: 1.0.0

# 该项的更改需要重启服务器才能生效
RegisterCommand:
  reload:
    enable: true
    alias: "none"
    permission: "yess.command.reload"
  help:
    enable: true
    alias: "none"
    permission: "yess.command.help"
  craft:
    enable: true
    alias: "craft"
    permission: "yess.command.craft"
  ec:
    enable: true
    alias: "ec"
    permission: "yess.command.ec"
  glow:
    enable: true
    alias: "glow"
    permission: "yess.command.glow"

# 存储类型: yaml, mysql 或 sqlite
storage:
  type: "yaml"  # 默认使用yaml

  # MySQL配置 (当storage.type=mysql时使用)
  mysql:
    host: "localhost"
    port: 3306
    database: "minecraft"
    username: "root"
    password: "password"

# 门
portal:
  world: "spawn"
  command:
    - "multiverse-core:mv tp %player% afk"
  regionMin:
    x: 43
    y: 72
    z: -2
  regionMax:
    x: 44
    y: 78
    z: 2

```

## PAPI

- %yess_glow_status% 玩家是否处于发光状态
