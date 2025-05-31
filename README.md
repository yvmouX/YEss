# YEss
基础功能插件

## CommandHelp

===== YEss v1.0.0 帮助 =====

- /yess help <player>
  - yess.command.help

- /yess ec <player>
  - yess.command.ec

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
  help:
    enable: true
    alias: "none"
  craft:
    enable: true
    alias: "craft"
  ec:
    enable: true
    alias: "ec"
```

