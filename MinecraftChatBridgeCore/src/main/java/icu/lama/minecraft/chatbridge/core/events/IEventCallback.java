package icu.lama.minecraft.chatbridge.core.events;

public interface IEventCallback <S, D> {
    void run(S source, D data);
}
