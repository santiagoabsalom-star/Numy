package com.surrogate.numy.views.home;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class QuoteBroadcaster {
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final LinkedList<Consumer<String>> listeners = new LinkedList<>();


    public static synchronized void register(Consumer<String> listener) {
        listeners.add(listener);
    }


    public static synchronized void unregister(Consumer<String> listener) {
        listeners.remove(listener);
    }


    public static synchronized void broadcast(String quote) {
        for (Consumer<String> listener : listeners) {
            executor.execute(() -> listener.accept(quote));
        }
    }
}
