package com.surrogate.numy.views.home.chathelper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.surrogate.numy.models.DTO.MensajeDTO;
import com.surrogate.numy.models.bussiness.Chat.Chat;
import com.surrogate.numy.models.bussiness.Chat.Mensaje;
import com.surrogate.numy.models.peticiones.Response;
import com.surrogate.numy.repository.bussiness.ChatRepository;
import com.surrogate.numy.repository.bussiness.UsuarioRepository;
import com.surrogate.numy.services.bussiness.ChatService;
import com.surrogate.numy.utils.UserDetailsWithId;
import com.vaadin.flow.server.VaadinSession;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
@Service
public class ChatHelper {
    private final Logger log = LoggerFactory.getLogger(ChatHelper.class);
    private final ChatService chatService;
    //TODO: IMPLEMENTAR ESTA SHITTTTT
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private WebSocketClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private BiConsumer<String, String> onMensajeRecibido;

    public ChatHelper(ChatService chatService, ChatRepository chatRepository, UsuarioRepository usuarioRepository) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public void conectar(String chat, BiConsumer<String, String> onMensajeRecibidoCallback) {
        this.onMensajeRecibido = onMensajeRecibidoCallback;
        String uri = "ws://localhost:3050/chat/" + "?" + "Nombre_Chat=" + chat + "&Emisor=" + obtenerNombreDeSesion();
        Map<String, String> headers = new HashMap<>();

        headers.put("User-Agent", "Vaadin-Client");

        try {
            client = new WebSocketClient(new URI(uri), new HashMap<>(headers)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {


                }

                @Override //Recibir
                public void onMessage(String message) {
                    try {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                }

                @Override
                public void onError(Exception ex) {
                }
            };

            client.connect();

        } catch (Exception e) {
        }
    }

    public void enviarMensaje(String contenido, String nombreChat, String emisor, String receptor) {
        if (client != null && client.isOpen()) {
            try {
                if (!chatRepository.existsByNombreChat(nombreChat)) {

                    log.error("El chat no existe,no se puede enviar el mensaje");
                }
                Chat chat = chatRepository.findBynombreChat(nombreChat);
                Mensaje mensaje = new Mensaje();
                mensaje.setContenido(contenido);
                mensaje.setId_chat(chat);
                mensaje.setFechaHora(LocalDateTime.now());
                mensaje.setEmisor(usuarioRepository.findByNombre(emisor));
                mensaje.setReceptor(usuarioRepository.findByNombre(receptor));
                client.send(mensaje.toString());


            } catch (Exception e) {

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
            }
        }

    }

    private String obtenerNombreDeSesion() {
        Authentication auth = VaadinSession.getCurrent().getAttribute(Authentication.class);
        UserDetailsWithId userDetails = (UserDetailsWithId) auth.getPrincipal();
       return userDetails.getUsername();
    }
}

