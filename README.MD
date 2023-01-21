# MinecraftChatBridge

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

一个转发MC聊天用的工具库，通过 `MinecraftChatBridgeCore` 来连接不同的MC平台（例如：`Bukkit` `Forge`等）和不同的聊天平台（例如：`telegram` `wechat`）

目前官方已有 `telegram`、`wechat`、`fabric`  平台官方支持，未来将加入 `Bukkit` `Forge` `Tencent-QQ` 等官方支持。

### 我是用户
哦我的上帝，别来看我啊，去找对应的MC支持的帮助文件看啊！我怎么会知道对应的MC支持需要用的配置文件是啥格式啊！

### 我想开发 MC 支持
您只需参考官方的 TestMinecraftBridge 进行开发即可，仅需要实现 接口`icu.lama.minecraft.chatbridge.core.minecraft.IMinecraftBridge` 即可

您还需要在您的 插件/mod... 加载的时候调用 `MinecraftChatBridge#init`，前两个参数为配置文件，需要您根据兼容的平台不同进行配置文件加载（比如`Bukkit`上使用`SnakeYaml(BukkitConfiguration)` 进行配置文件加载）

### 我想开发某聊天平台支持
您只需参考官方的 TelegramPlatformBridge 进行开发即可，您需要实现 `icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge` 这个接口并在 resources 下放置一个叫`info`的文件
并写入以下信息
```properties
mainClass=您的主类名称（即实现了 IPlatformBridge 接口的类)
```
