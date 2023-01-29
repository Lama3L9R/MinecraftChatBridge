package icu.lama.minecraft.chatbridge.core.events.platform;

import icu.lama.minecraft.chatbridge.core.events.EventBase;
import icu.lama.minecraft.chatbridge.core.events.IEventCallback;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;

public class PlatformEvent <T> extends EventBase<T, IEventCallback<IPlatformBridge, T>, IPlatformBridge > {
}
