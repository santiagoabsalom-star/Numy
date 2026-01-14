package com.surrogate.numy.views.home.chathelper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
@Service
public class ChatHelper {
    //TODO: IMPLEMENTAR ESTA SHITTTTT
    private WebSocketClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private BiConsumer<String, String> onMensajeRecibido;


    public void conectar(String chat, BiConsumer<String, String> onMensajeRecibidoCallback) {
        this.onMensajeRecibido = onMensajeRecibidoCallback;

        String uri = "ws://localhost:3050/chat/" + chat;
        Map<String, String> headers = new HashMap<>();

        headers.put("User-Agent", "Vaadin-Client");

        try {
            client = new WebSocketClient(new URI(uri) , new HashMap<>(headers)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("✓ Conectado a la chat: " + chat);

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

    public void enviarMensaje(String contenido, String chat) {
        if (client != null && client.isOpen()) {
            try {
client.send(contenido);

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
    }}

