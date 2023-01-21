# FabricChatBridge

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

_官方支持的`Fabric Server`链接桥_

## 我是用户

1. 首先将Mod本体文件（未来提供预编译下载）放入 `mods` 文件夹.
2. 紧接着，创建一个配置文件 `config/MinecraftChatBridge.json`并参考下方默认配置文件进行修改后写入其中。
3. 将 `PlatformBridge` 放入配置文件中填写的目录即可开服使用。

若遇到任何Bug、功能改善，欢迎提交 Issue 或者 Pull-Request 向这个项目进行贡献。

### 配置文件
请根据不同的 `PlatformBridge` 使用标准 `json` 格式填写所需内容，所有 `PlatformBridge` 配置项应写在 `platformConf` 下。
其中 `format` 按照 `String#format` 格式进行书写，第一个参数为平台名，第二个参数是玩家名，第三个参数是玩家发的消息。
`core.bridgeDirectory` 配置项为 `PlatformBridge` 存放目录，为了保证规范性最好不进行修改，保持下方示例配置文件值即可。
```json
{
  "core": {
    "bridgeDirectory": "config/ChatPlatformBridges"
  },
  "platformConf": { },
  "format": "[platform: %1$s] <player: %2$s> says: %3$s"
}
```

下方是一个使用官方 `telegram` 平台支持的示例文件

```json
{
  "core": {
    "bridgeDirectory": "config/ChatPlatformBridges"
  },
  "platformConf": {
    "telegram": {
      "extraContents": {
        "chatId": -10000000000
      },
      "trustedCallers": [],
      "credentials": {
        "botToken": "<YourBotToken>"
      }
    }
  },
  "format": "<%2$s> %3$s"
}
```

## 我是开发者

欢迎开PR :)

