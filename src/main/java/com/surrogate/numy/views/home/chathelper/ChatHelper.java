package com.surrogate.numy.views.home.chathelper;


import com.surrogate.numy.utils.UserDetailsWithId;
import com.vaadin.flow.server.VaadinSession;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class ChatHelper {
    private final Logger log = LoggerFactory.getLogger(ChatHelper.class);

    //TODO: IMPLEMENTAR ESTA SHITTTT

    private WebSocketClient client;
    private Consumer<String> receptor;

    public ChatHelper() {




    }


    public void conectar(String chat, Consumer<String> onMensajeRecibidoCallback) {
        this.receptor = onMensajeRecibidoCallback;
        String uri = "ws://localhost:3050/chat/" + "?" + "Nombre_Chat=" + chat + "&Emisor=" + obtenerNombreDeSesion();
        Map<String, String> headers = new HashMap<>();

        headers.put("User-Agent", "Vaadin-Client");

        try {
            client = new WebSocketClient(new URI(uri), new HashMap<>(headers)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info(handshakedata.getHttpStatusMessage());



                }

                @Override //Recibir
                public void onMessage(String message) {
                    try {
                    receptor.accept(message);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

                @Override

                public void onClose(int code, String reason, boolean remote) {
                    log.info("Conexion cerrada por {}",reason);
                }

                @Override
                public void onError(Exception ex) {
                    log.error(ex.getMessage());
                }
            };

            client.connect();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void enviarMensaje(String contenido) {
        if (client != null && client.isOpen()) {
            try {
                client.send(contenido);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            assert client != null;
        }
    }

    public void cerrarConexion() {
        if (client != null) {
            try {
                client.close(1000, "Cierre de conexión solicitado");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    private String obtenerNombreDeSesion() {
        Authentication auth = VaadinSession.getCurrent().getAttribute(Authentication.class);
        UserDetailsWithId userDetails = (UserDetailsWithId) auth.getPrincipal();
       return userDetails.getUsername();
    }
}

