package icu.lama.minecraft.chatbridge.core.events.platform;

import icu.lama.minecraft.chatbridge.core.events.manager.IEventCallback;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

public interface PlatformEventCallback <T> extends IEventCallback<IPlatformProxy, T> { }