package com.surrogate.numy.views.home;

import com.surrogate.numy.views.login.LoginView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;




@Route("")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {
    Span quote= new Span();

    private static final Logger log = LoggerFactory.getLogger(HomeView.class);
    private HomeView(){
        setSizeFull();


        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");
        getStyle().set("background-repeat", "no-repeat");
        Div logoutDiv = new Div();

        Div divButtons = new Div();
        Div container = new Div();
        Div quoteOfTheDay= new Div();
        quoteOfTheDay.addClassName("quote-of-the-day-div");
        container.addClassName("image-container");
        Span quoteTitle= new Span("Quote of the day");

        container.add(logoutDiv,divButtons, quoteOfTheDay);
        quoteTitle.addClassName("quote-title");
        quote.addClassName("quote");

        quoteOfTheDay.add(quoteTitle, quote);
        logoutDiv.setClassName("div-buttons-logout");
       divButtons.setClassName("div-buttons");
        Button logout= new Button(new Icon(VaadinIcon.ARROW_LEFT));
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        logout.addClickListener(e -> {
          SecurityContextHolder.clearContext();
          VaadinSession.getCurrent().close();

           UI.getCurrent().navigate(LoginView.class);
        });
        Button chat = new Button(new Icon(VaadinIcon.CHAT));

        chat.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button notificaciones = new Button(new Icon(VaadinIcon.BELL));
        notificaciones.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        chat.addClickListener(event -> notificaciones.setVisible(!notificaciones.isVisible()));

        logoutDiv.add(logout);
        divButtons.add(chat, notificaciones);
        add(container);

    }







    @Override
        public void beforeEnter(BeforeEnterEvent event) {
            if (!checkAuth()) {
                log.debug("Login failed");
                UI.getCurrent().getPage().setLocation("/login");
            }

        }


    private boolean checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            auth = VaadinSession.getCurrent().getAttribute(Authentication.class);
        }

        return auth != null && auth.isAuthenticated() &&
                auth instanceof UsernamePasswordAuthenticationToken;
    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();


        QuoteBroadcaster.register(nuevaFrase -> {
            ui.access(() -> quote.setText(nuevaFrase));
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {

    }
    }

