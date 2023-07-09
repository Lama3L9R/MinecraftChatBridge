package icu.lama.minecraft.chatbridge.core.events.manager;

import java.util.ArrayList;
import java.util.List;

public abstract class EventBase<D, C extends IEventCallback<S, D>, S> {
    private final List<C> callbacks = new ArrayList<>();

    /**
     * Trigger this event.
     * @param args event data
     */
    public void trigger(S source, D args) {
        callbacks.forEach(it -> it.run(source, args));
    }

    /**
     * Subscribe to this event
     * @param callback event handler
     * @return key of handler, use for unsubscribe
     */
    public int subscribe(C callback) {
        for (int i = 0; i < callbacks.size(); i++) {
            if (callbacks.get(i) == null) {
                callbacks.set(i, callback);
                return i;
            }
        }
        callbacks.add(callback);
        return callbacks.size() - 1;
    }

    /**
     * cancel event listening of handler
     * @param key key of handler
     */
    public void unsubscribe(int key) {
        if (key < 0) {
            return;
        }
        callbacks.set(key, null);
    }
}
