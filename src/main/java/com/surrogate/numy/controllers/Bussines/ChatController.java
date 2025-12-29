package com.surrogate.numy.controllers.Bussines;

import com.surrogate.numy.models.DTO.ChatDTO;
import com.surrogate.numy.models.DTO.MensajeDTO;
import com.surrogate.numy.models.bussiness.Chat.Chat;
import com.surrogate.numy.models.peticiones.Response;
import com.surrogate.numy.services.bussiness.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/crearChat")
    public ResponseEntity<Response> crearChat(@RequestBody Chat chat) {
        Response response = chatService.crearChat(chat);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/eliminarChat")
    public ResponseEntity<Response> eliminarChat(@RequestBody Chat chat) {
        Response response = chatService.eliminarChat(chat);
        return ResponseEntity.status(response.getHttpCode()).body(response);


    }

    @GetMapping("/obtenerChatsPorUsuario")
    public ResponseEntity<List<ChatDTO>> obtenerChatsPorUsuario(@RequestParam Long id_usuario) {

        List<ChatDTO> chats = chatService.obtenerChatsDeUsuario(id_usuario);
        if (chats == null || chats.isEmpty()) {
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok(chats);
    }
    @GetMapping("/obtenerChatsPorUsuarioCurrent")
    public ResponseEntity<List<ChatDTO>> obtenerChatsPorUsuarioCurrent(HttpServletRequest request) {

        String token = getTokenFromRequest(request);
        List<ChatDTO> chats = chatService.obtenerChatsDeUsuarioCurrent(token);
        if (chats == null || chats.isEmpty()) {
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok(chats);
    }
    @GetMapping("/obtenerUltimoMensajePorChat")
    public ResponseEntity<MensajeDTO> obtenerUltimoMensajePorChat(@RequestParam Long id_chat) {
        MensajeDTO mensaje = chatService.ultimoMensaje(id_chat);
        if (mensaje == null) {
            return ResponseEntity.ofNullable(null);

        }
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/obtenerMensajesPorChat")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesPorChat(@RequestParam Long id_chat) {
        List<MensajeDTO> mensajes = chatService.obtenerMensajesDeChat(id_chat);
        if (mensajes == null || mensajes.isEmpty()) {
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok(mensajes);
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length == 0) {

            return null;
        }

        return Objects.requireNonNull(Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("AUTH_TOKEN")).findFirst().orElse(null)).getValue();
    }


}


