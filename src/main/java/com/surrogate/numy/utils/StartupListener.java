package com.surrogate.numy.utils;

import com.surrogate.numy.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupListener {

    private final AuthService authService;


    @EventListener(ApplicationReadyEvent.class)
    void onAppReady() {
        authService.createSystemAdminIfNotExists();
    }
}

