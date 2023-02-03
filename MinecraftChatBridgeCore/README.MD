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

通常来说，你需要写一个 Mod/Plugin，因此你得会写模组/插件。然后将 MinecraftChatBridgeCore 作为依赖打包进你的模组/插件。
下面的分区写明了你必须实现的东西和可选实现，必须实现正如字面意思他是必须的，可选实现正如字面意思他是可选的。
是否实现可选部分取决于服务端实际情况，比如是否支持此操作、是否安全、是否会造成严重性能问题等。

### 必须实现

#### CoreLoader
这部分是自由的，没有任何的接口、抽象类约束，你只需要写一个配置文件加载来加载 Core 和其他平台的配置文件。
下面是来自 FabricChatBridge 中的配置文件结构，可以用作参考

```java
public class ModConfig {
   public ChatBridgeConfiguration core; // Core 配置文件
   public Map<String, PlatformConfiguration> platformConf; // 平台配置文件
   public String format; // MC中显示转发聊天信息的格式
}
```

然后您需要调用 `MinecraftChatBridge#init`，强烈建议顺路实现 `errorHandler`。
下面是来自 FabricChatBridge 中的一段代码，可以用作参考

```
MinecraftChatBridge.init(config.core, config.platformConf, this, (e, source) -> this.LOGGER.error("Runtime error thrown by " + source.getPlatformName(), e));
```

一个完整的加载方法大概长这样：
```
ModConfig config = this.GSON.fromJson(new FileReader(this.configFile), new TypeToken<>() {});
if (config.format != null && !config.format.isEmpty()) {
   LOGGER.info("Override default format to: " + config.format);
   this.format = config.format;
} else {
   LOGGER.info("Missing format, using default one: " + format);
}

MinecraftChatBridge.init(config.core, config.platformConf, this, (e, source) ->
    this.LOGGER.error("Runtime error thrown by " + source.getPlatformName(), e)
);
LOGGER.info("MinecraftChatBridge initialized complete");
```

这时您应该会注意到 `MinecraftChatBridge#init` 中有一个参数 `@NotNull IMinecraftBridge bridg` 不知道从哪里来。
别急，请继续向下看

#### 实现 `IMinecraftBridge`

`IMinecraftBridge` 中有两个方法需要您实现，`send` 方法您需要根据服务端不同进行不同的实现，细节可以查看 JavaDoc 和 FabricMinecraftBridge

在这里重点想说一下 `setReceiveCallback`，这个方法的实现通常来说很固定，一般都如下方代码，通常情况下，您也可以照抄下面的代码

```java
class ExampleBridge implements IMinecraftBridge {
    private MinecraftReceiveCallback callback;
    
    // 已省略 send 实现
    @Override public void setReceiveCallback(MinecraftReceiveCallback callback) {
        this.callback = callback;
    }
}
```

您应该在收到玩家发送的消息之后执行这个回调函数，下面是来自 FabricChatBridge 中的一段代码，可以用作参考

```
ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) ->
    this.callback.onReceive(sender.getName().getString(), sender.getUuid(), message.getSignedContent())
);
```

您应该将您的实现实例化（理应是单例）并传递到 `MinecraftChatBridge#init` 中

### 可选实现

#### 事件系统

老实说你真的有能力实现上面提到的所有必须实现，大概我给你看个实例代码你也能明白怎么实现这些。
一句话快速说明就是你需要在对应的服务端事件触发对应的 MinecraftChatBridgeCore 中的事件。
```
ServerLifecycleEvents.SERVER_STARTED.register((server) -> { // 注册服务端事件处理器
   this.serverInstance = server;

   // 触发 MinecraftChatBridgeCore 中的对应的事件
   MinecraftEvents.onServerSetupComplete.trigger(null, server);
});

ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
   MinecraftEvents.onServerBeginShutdown.trigger(null, null);
});

ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
   ServerPlayerEntity sPlayer = handler.getPlayer();
   MinecraftEvents.onPlayerJoin.trigger(new MinecraftEventSource(sPlayer.getUuid(), sPlayer.getName().getString(), false), null);
});

ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
   ServerPlayerEntity sPlayer = handler.getPlayer();
   MinecraftEvents.onPlayerLeave.trigger(new MinecraftEventSource(sPlayer.getUuid(), sPlayer.getName().getString(), false), null);
});
```

#### 服务器代理

因为不同的服务端相关接口也不一样，有些时候platform希望获取一些服务端的信息比如在线玩家、motd等时就尴尬了。
为了解决这个尴尬情况，我加入了 `IMinecraftServerProxy`，这样所有的 platform 都可以在任何服务端上调用服务端的api了。

老实说你真的有能力实现上面提到的所有必须实现，大概你看看里面标注的 JavaDoc 应该能明白你要实现的东西，
省流：
实现 `IMinecraftServerProxy` 中除了被 `@Risky` 标注的以外的所有方法，
实现 `IProxyPlayer` 中除了被 `@Risky` 标注的以外的所有方法

所有被 `@Risky` 标注的方法如果可以的话最好还是实现，但如果实在是因为服务端不支持也好、不安全也好，总之不能实现，
那么直接抛出 `UnsupportedOperationException` 即可


## 我是开发者，我希望开发某聊天平台支持
相比于开发 MinecraftBridge 来说，开发聊天平台通常要简单的多。

先来看一个省流版本：
1. impl `IPlatformBridge`
2. resources/info
```properties
# ExamplePlatformBridge implements IPlatformBridge 
mainClass=org.example.ExamplePlatformBridge
```
3. call onPlatformBridgeLoad event via: `PlatformEvents.onPlatformBridgeLoad.trigger`


### 我流量多
1. 实现 `icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge` 这个接口
2. 在 resources 下放置一个叫`info`的文件 并写入以下信息
```properties
#完整主类名字（即实现了 IPlatformBridge 接口的类)
mainClass=org.example.ExamplePlatformBridge
```

### 其他接口

#### 事件系统示例代码：
```
MinecraftEvents.onPlayerJoin.subscribe((source, d) -> {
    // event handler
});
```
#### ServerProxy 示例代码：
**注意，ServerProxy因为不是必须实现的内容，因此可能在某些地方无法使用，注意Javadocs里的@Nullable。**
**除此之外还要注意标记了`@Risky`的方法、字段，因为这些成员可能在某些服务端上即使实现了 ServerProxy 也可能因为某些原因不去实现这几个标记了 `@Risky` 的方法**
```
IMinecraftServerProxy server = MinecraftChatBridge.getServerProxy();
if (server == null) { // feature not supported
    return giveUp(CoderEmotions.SAD);
} else { // feature is supported
    server.getPlayer("Lama3L9R").kick()
}

try { // @Risky 标记的方法需要 try-catch
    // use broadcastPacket(Ljava/lang/Object;)V
    server.broadcastPacket(new ExampleNettyPacket());
} catch (UnsupportedOperationException e) {
    // 当前实现不支持向玩家广播数据包
    // 对于发包接口多提一嘴，有些不是走 Netty 的服务端实现有可能只支持其中一个数据包发送方法
    // 理论上来说都应该支持 broadcastPacket([B)V
    // 当然如果开发者偷懒了没实现也是有可能的（
    
    try { // use broadcastPacket([B)V
        server.broadcastPacket(NBT.nbtify(new ExampleNettyPacket()).getBytes());
    } catch(UnsupportedOperationException e) {
        // 这下是都不支持，只能放弃
        return giveUp(CoderEmotions.SAD);
    }
} 
```
