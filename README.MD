# MinecraftChatBridge

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

一个转发MC聊天用的工具库，通过 `MinecraftChatBridgeCore` 来连接不同的MC平台（例如：`Bukkit` `Forge`等）和不同的聊天平台（例如：`telegram` `wechat`）

目前官方已有 `telegram`、`wechat`、`fabric`  平台官方支持，未来将加入 `Bukkit` `Forge` `Tencent-QQ` 等官方支持。

### 我是用户
自从 ChatBridge 2.0.0 起配置文件已经全面统一为 HOCON！

### 我想开发 MC 支持
您只需参考官方的 TestMinecraftBridge 进行开发即可，仅需要实现 接口 `icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy` 即可

您还需要在您的 插件/mod... 加载的时候调用 `MinecraftChatBridge#init`，

### 我想开发 聊天平台 支持
参考 TelegramChatBridge 进行开发