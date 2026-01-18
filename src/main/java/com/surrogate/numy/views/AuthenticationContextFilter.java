package com.surrogate.numy.views;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationContextFilter implements VaadinServiceInitListener {
    private final Logger log= org.slf4j.LoggerFactory.getLogger(AuthenticationContextFilter.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionEvent -> sessionEvent.getSession().addRequestHandler((session, request, response) -> {
            Authentication auth = session.getAttribute(Authentication.class);
            log.info("Restoring authentication for request: {}", request.getPathInfo());
            log.info("id de sesion: {}", session.getPushId());
            log.info("Nombre de sesion: {}", session.getSession().getAttribute("nombre-usuario"));
            if (auth != null) {

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Nombre en auth: {}", auth.getName());
            }
            return false;
        }));
    }
}


