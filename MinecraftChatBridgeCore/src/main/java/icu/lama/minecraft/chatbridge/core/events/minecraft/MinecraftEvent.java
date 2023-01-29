package icu.lama.minecraft.chatbridge.core.events.minecraft;

import icu.lama.minecraft.chatbridge.core.events.EventBase;
import icu.lama.minecraft.chatbridge.core.events.IEventCallback;

public class MinecraftEvent <T> extends EventBase<T, IEventCallback<MinecraftEventSource, T>, MinecraftEventSource> { }
