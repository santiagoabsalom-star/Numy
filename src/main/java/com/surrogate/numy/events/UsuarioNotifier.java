package com.surrogate.numy.events;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UsuarioNotifier {

    private final List<Consumer<UsuarioCreado>> listeners = new ArrayList<>();

    public void addListener(Consumer<UsuarioCreado> listener) {
        listeners.add(listener);
    }

    public void publish(UsuarioCreado event) {
        listeners.forEach(l -> l.accept(event));
    }
}
