package com.surrogate.numy.views;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationContextFilter implements VaadinServiceInitListener {


    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionEvent -> sessionEvent.getSession().addRequestHandler((session, request, response) -> {
            Authentication auth = session.getAttribute(Authentication.class);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            return false;
        }));
    }
}


