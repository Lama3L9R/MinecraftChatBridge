package icu.lama.minecraft.chatbridge.core.events.platform;

import icu.lama.minecraft.chatbridge.core.events.manager.EventBase;
import icu.lama.minecraft.chatbridge.core.events.manager.IEventCallback;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

public class PlatformEvent <T> extends EventBase<T, IEventCallback<IPlatformProxy, T>, IPlatformProxy > {
}
