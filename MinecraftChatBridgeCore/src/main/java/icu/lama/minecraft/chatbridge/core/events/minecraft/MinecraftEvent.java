package icu.lama.minecraft.chatbridge.core.events.minecraft;

import icu.lama.minecraft.chatbridge.core.events.manager.EventBase;
import icu.lama.minecraft.chatbridge.core.events.manager.IEventCallback;

public class MinecraftEvent <T> extends EventBase<T, IEventCallback<MinecraftEventSource, T>, MinecraftEventSource> { }
