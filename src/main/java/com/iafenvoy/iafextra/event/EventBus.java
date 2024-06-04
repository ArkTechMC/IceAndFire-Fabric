package com.iafenvoy.iafextra.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private static final HashMap<Class<? extends Event>, List<Consumer<Event>>> EVENTS = new HashMap<>();

    public static boolean post(Event event) {
        for (Map.Entry<Class<? extends Event>, List<Consumer<Event>>> entry : EVENTS.entrySet()) {
            if (entry.getKey() == event.getClass()) {
                entry.getValue().forEach(x -> x.accept(event));
                if(event.isCanceled()) return true;
            }
        }
        return false;
    }

    public static <T extends Event> void register(Class<T> clazz, Consumer<Event> event) {
        if (!EVENTS.containsKey(clazz)) EVENTS.put(clazz, new ArrayList<>());
        EVENTS.get(clazz).add(event);
    }
}
