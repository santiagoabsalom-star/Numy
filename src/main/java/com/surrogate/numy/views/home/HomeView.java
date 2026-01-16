package com.surrogate.numy.views.home;

import com.surrogate.numy.models.DTO.ConexionDTO;
import com.surrogate.numy.models.DTO.MensajeDTO;
import com.surrogate.numy.repository.bussiness.ChatRepository;
import com.surrogate.numy.repository.bussiness.ConexionRepository;
import com.surrogate.numy.repository.bussiness.MensajeRepository;
import com.surrogate.numy.views.home.chathelper.ChatHelper;
import com.surrogate.numy.views.login.LoginView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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

import java.util.List;


@Route("")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {
    private final ChatHelper chatHelper;
    private final MensajeRepository mensajeRepository;
    private final ChatRepository chatRepository;
    private final ConexionRepository conexionRepository;
    Span quote = new Span();
    Span quoteTitle = new Span();
    private String nombreUsuarioActual;
    private static final Logger log = LoggerFactory.getLogger(HomeView.class);

    private HomeView(QuoteService quoteService, ChatHelper chatHelper, MensajeRepository mensajeRepository, ChatRepository chatRepository, ConexionRepository conexionRepository) {
        this.conexionRepository=conexionRepository;
        this.mensajeRepository=mensajeRepository;
        this.chatRepository=chatRepository;
        this.chatHelper = chatHelper;
        setSizeFull();
        quoteService.loadFirstQuote(quote);


        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");
        getStyle().set("background-repeat", "no-repeat");
        Image conejos= new Image();
        conejos.setSrc("images/conejos.png");
        conejos.setClassName("conejos");
        Image fuk= new Image();

        fuk.setSrc("images/mariconpro.png");
        fuk.setClassName("image");

//        width:10%;
//        height: 20%;
        Div image = new Div();

        image.setClassName("image");
        Div chatDiv = Chat();
        chatDiv.setVisible(false);
        Div divButtons = new Div();
        Div container = new Div();
        Div quoteOfTheDay = new Div();
        quoteOfTheDay.addClassName("quote-of-the-day-div");
        container.addClassName("image-container");
        quoteTitle.setText("Quote of the day");

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);

        Icon profileIcon = VaadinIcon.USER.create();
        profileIcon.setClassName("icons");
        profileIcon.setColor("white");
        MenuItem profile = menuBar.addItem(profileIcon);
        SubMenu profileSubMenu = profile.getSubMenu();
        MenuItem chatItem = profileSubMenu.addItem(VaadinIcon.CHAT.create());
        chatItem.addClickListener(event -> {

            boolean estaVisible = !chatDiv.isVisible();

            chatDiv.setVisible(estaVisible);

            if (estaVisible) {
                initDragLogic();
            }
        });
        profileSubMenu.getItems();
        profileSubMenu.addItem(VaadinIcon.SIGN_OUT.create(), menuItemClickEvent -> {

            SecurityContextHolder.clearContext();
            VaadinSession.getCurrent().close();

            UI.getCurrent().navigate(LoginView.class);
        });


        quoteTitle.addClassName("quote-title");
        quote.addClassName("quote");

        quoteOfTheDay.add(quoteTitle, quote);

        divButtons.setClassName("div-buttons");


        Button chat = new Button();
        Icon notification= new Icon(VaadinIcon.BELL);
        notification.setClassName("icons");
        notification.setColor("white");

        chat.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Button notificaciones = new Button();
        notificaciones.setIcon(notification);
        Icon chatIcon = new Icon(VaadinIcon.CHAT);
        chatIcon.setClassName("icons");
        chatIcon.setColor("white");
        chat.setIcon(chatIcon);
        notificaciones.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        chat.addClickListener(event -> notificaciones.setVisible(!notificaciones.isVisible()));


        divButtons.add(menuBar, chat, notificaciones,conejos);
        container.add( divButtons, quoteOfTheDay);
        add(container, chatDiv,fuk);

    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!checkAuth()) {
            log.debug("Login failed");

            UI.getCurrent().getPage().setLocation("/login");
        }
        nombreUsuarioActual= (String)VaadinSession.getCurrent().getAttribute("nombre-usuario");
        //  UserPrincipal userPrincipal = (UserPrincipal)   VaadinSession.getCurrent().getAttribute(Authentication.class).getPrincipal();
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


        QuoteBroadcaster.register(nuevaFrase -> ui.access(() -> quote.setText(nuevaFrase)));

    }


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        UI ui = detachEvent.getUI();
        QuoteBroadcaster.unregister(nuevaFrase -> ui.access(() -> quote.setText(nuevaFrase)));

    }

    private Div Chat() {
        Div chat = new Div();


        HorizontalLayout mensajesContainer = new HorizontalLayout();

        mensajesContainer.setClassName("mensajes-container");
        Span errorSpan = new Span();

        String nombreUsuarioReceptor;
        ConexionDTO conexionDto= conexionRepository.findConexionByNombreUsuario(nombreUsuarioActual);

        if(conexionDto!=null) {
            if(conexionDto.nombreUsuario1().equals(nombreUsuarioActual)){
                nombreUsuarioReceptor = conexionDto.nombreUsuario2();
            }
            else {
                nombreUsuarioReceptor = conexionDto.nombreUsuario1();
            }

            String nombreChat= conexionDto.nombreUsuario1()+"_"+conexionDto.nombreUsuario2();

            Long idChat= chatRepository.findChatId(nombreChat);


            List<MensajeDTO> mensajes= mensajeRepository.findAllByChatId(idChat);
            for(MensajeDTO mensaje: mensajes) {
                Div mensajeDiv = new Div();
                Span nombreUsuario= new Span();
                if(mensaje.emisor().equals(nombreUsuarioActual)){
                    nombreUsuario.setText(nombreUsuarioActual);

                    Span mensajeSpan = new Span();
                    mensajeSpan.setText(mensaje.contenido());
                    mensajeDiv.add(nombreUsuario,mensajeSpan);

                    mensajeDiv.setClassName("mensaje-usuario-actual");

                    mensajesContainer.add(mensajeDiv);
                }
                else{
                    nombreUsuario.setText(nombreUsuarioReceptor);
                    Span mensajeSpan = new Span();
                    mensajeSpan.setText(mensaje.contenido());
                    mensajeDiv.add(nombreUsuario,mensajeSpan);
                    mensajeDiv.setClassName("mensaje-usuario-receptor");
                    mensajesContainer.add(mensajeDiv);
                }
            }
                mensajesContainer.addAttachListener(attachEvent -> {
                    UI ui = attachEvent.getUI();

                    chatHelper.conectar(nombreChat,(message)->{
                    ui.access(() -> {     Div mensajeDiv = new Div();

                        mensajeDiv.setClassName("mensaje-usuario-receptor");
                        Span nombreUsuario= new Span();
                        nombreUsuario.setText(nombreUsuarioReceptor);
                        Span mensajeSpan = new Span();

                        mensajeSpan.setText(message);
                        mensajeDiv.add(nombreUsuario,mensajeSpan);
                    });


                });

                });
                mensajesContainer.addDetachListener(detachEvent -> chatHelper.cerrarConexion());
        }
        else{
            mensajesContainer.addAttachListener(attachEvent -> {
                UI ui = attachEvent.getUI();
               ui.access(() -> errorSpan.setText("Conecta con alguien para poder usar el chat")) ;
            });
            mensajesContainer.add(errorSpan);
        }


        chat.setId("chat");
        chat.setClassName("chat");
        Div chatSelector = new Div();
        Button chatButton = new Button();
        chatSelector.add(chatButton);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE);
        closeIcon.setColor("red");

        Button closeButton = new Button(closeIcon);

        closeButton.setClassName("close-button");

        closeButton.addClickListener(buttonClickEvent -> chat.setVisible(false));
        Div handle = new Div();
        handle.setId("handle");

        handle.setClassName("handle");


        Span chatTitle = new Span();

        TextArea chatText = new TextArea();
        chatText.setClassName("chat-text-area");
        Icon sendIcon = new Icon(VaadinIcon.CHECK);
        sendIcon.setColor("white");
        Button sendButton = new Button(sendIcon);

        sendButton.setClassName("send-button");

        sendButton.addClickListener(event -> {
        chatHelper.enviarMensaje(chatText.getValue());
        Div mensajeDiv = new Div();
        mensajeDiv.setClassName("mensaje-usuario");
        Span nombreUsuario= new Span();
        nombreUsuario.setText(nombreUsuarioActual);
        Span mensajeSpan = new Span();
        mensajeSpan.setText(chatText.getValue());
        mensajeDiv.add(nombreUsuario,mensajeSpan);
        mensajesContainer.add(mensajeDiv);
            chatText.clear();
        });
        chat.add(handle,mensajesContainer, chatText, sendButton, closeButton);


        return chat;
    }



    private void initDragLogic() {
        getElement().executeJs(
                "const el = document.getElementById('chat');" +
                        "const handle = document.getElementById('handle');" +

                        "if (el && handle) {" +
                        "  let mouseX = 0, mouseY = 0;" +
                        "  let targetTop = el.offsetTop;" +
                        "  let targetLeft = el.offsetLeft;" +
                        "  let dragging = false;" +

                        "  function updatePosition() {" +
                        "    if (!dragging) return;" +

                        "    el.style.top = targetTop + 'px';" +
                        "    el.style.left = targetLeft + 'px';" +

                        "    requestAnimationFrame(updatePosition);" +
                        "  }" +

                        "  handle.onmousedown = (e) => {" +
                        "    e.preventDefault();" +
                        "    dragging = true;" +
                        "    mouseX = e.clientX;" +
                        "    mouseY = e.clientY;" +
                        "    targetTop = el.offsetTop;" +
                        "    targetLeft = el.offsetLeft;" +


                        "    requestAnimationFrame(updatePosition);" +

                        "    const onMouseMove = (e) => {" +
                        "      const dx = mouseX - e.clientX;" +
                        "      const dy = mouseY - e.clientY;" +
                        "      mouseX = e.clientX;" +
                        "      mouseY = e.clientY;" +

                        "      targetTop = targetTop - dy;" +
                        "      targetLeft = targetLeft - dx;" +
                        "    };" +

                        "    const onMouseUp = () => {" +
                        "      dragging = false;" +
                        "      document.removeEventListener('mousemove', onMouseMove);" +
                        "      document.removeEventListener('mouseup', onMouseUp);" +
                        "    };" +

                        "    document.addEventListener('mousemove', onMouseMove);" +
                        "    document.addEventListener('mouseup', onMouseUp);" +
                        "  };" +
                        "}"
        );
    }

}



