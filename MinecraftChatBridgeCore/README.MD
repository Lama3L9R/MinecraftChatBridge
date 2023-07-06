# MinecraftChatBridgeCore

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
---

本项目的核心库，用于链接不同的mc服务器和不同的聊天平台，以达到在任意平台都可以转发聊天的效果

Core Library of this project. This library is used for connecting different Minecraft Servers and different Chat Platforms.
The goal of this project is to relay chats in whatever server and whatever chat platform.

I'm sorry for English users that I have no time to translate this readme.
However all java docs are in English(If you saw a grammar mistake ignore it or open a issue).

There are some official maintained subprojects can be seen as Example project.

---

## 项目结构
项目分为两个部分：MinecraftBridge和PlatformBridge

通俗的说，MinecraftBridge 是将不同的服务器实现接口统一到 MinecraftChatBridgeCore 中相关接口。
而 PlatformBridge 是将不同的聊天平台接口统一到 MinecraftChatBridgeCore 中相关接口。

有了这两个统一之后，就可以进行任意mc服务器和聊天平台的组合转发了

## 我是用户
**你不应该看这里！快离开！**

如果你想了解其中的技术细节的话... ~~算了你还是别了解了，我嫌丢人（~~

## 我是开发者，我希望开发 MinecraftBridge

介于整个项目结构都有所更改，故在此仅写明思路，未来将补充详细的内容

项目分为三个部分 Core, PlatformProxy 和 MinecraftProxy

其实非常字面意思，PlatformProxy相当于一个统一的接口，你需要做的仅仅是将目标平台的API转换到这个接口

MinecraftProxy同理

同时，为了兼容更多的情况。比如对 EasyAuth 的兼容（加入强制绑定平台账号才可以注册），我特地的模糊了 PlatformProxy 这个概念。

你既可以走 ChatBridge 的插件加载系统，也可以走 Minecraft 的 mod / 插件加载系统，这个看你喜好和需求

我也在尽可能实现热重载，这样可以稍微的能在mod服上舒服一些。