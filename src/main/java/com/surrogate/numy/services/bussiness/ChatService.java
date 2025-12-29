package com.surrogate.numy.services.bussiness;

import com.surrogate.numy.models.DTO.ChatDTO;
import com.surrogate.numy.models.DTO.MensajeDTO;
import com.surrogate.numy.models.bussiness.Chat.Chat;
import com.surrogate.numy.models.bussiness.Chat.Mensaje;
import com.surrogate.numy.models.peticiones.Response;
import com.surrogate.numy.repository.bussiness.ChatRepository;
import com.surrogate.numy.repository.bussiness.MensajeRepository;
import com.surrogate.numy.repository.bussiness.UsuarioRepository;
import com.surrogate.numy.services.auth.JWT.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;
    private final String success;
    private final String error;
    private final JWTService jwtService;
    private final UsuarioRepository usuarioRepository;


    @Transactional
    public Response crearChat(Chat chat) {
        Response response = verificarChat(chat);
        chat.setNombreChat(chat.getUsuario().getNombre() + "_" + chat.getAdministrador().getNombre());
        if (chatRepository.existsByNombreChat(chat.getNombreChat())) {
            return new Response(success, 409, "El chat ya existe");
        }
        if (response.getHttpCode() != 200) {
            return response;
        }

        try {
            chatRepository.save(chat);
        } catch (DataIntegrityViolationException e) {
            return new Response(success, 409, "El chat ya existe");
        }

        return new Response(success, 200,"Chat creado correctamente");
}
    public Response verificarChat(Chat chat){
        if( Thread.currentThread().getStackTrace()[1].getMethodName().equals("crearChat") && chatRepository.existsByNombreChat(chat.getUsuario().getNombre()+"_"+chat.getAdministrador().getNombre())){
        return new Response(error, 409, "El chat ya existe");

    }
        if(chat.getAdministrador().getNombre().equals(chat.getUsuario().getNombre())){
            return new Response(error, 409, "No puedes crear un chat contigo mismo");

        }
        if(chatRepository.existsByNombreChat(chat.getUsuario().getNombre())){
            return new Response(error, 409, "El chat ya existe");
        }


        return new Response(success, 200, "Operacion hecha correctamente");

    }
    //Este metodo se va a usar para actualizar el nombre del chat en caso de que se cambie el nombre de algun usuario
    public Response actualizarChat(Chat chat){

        Response response = verificarChat(chat);
        if(response.getHttpCode()!=200){
            return response;
        }
        chat.setNombreChat(chat.getUsuario().getNombre()+"_"+chat.getAdministrador().getNombre());
        chatRepository.save(chat);
        return response;

    }
    public Response eliminarChat(Chat chat){
        if(!chatRepository.existsByNombreChat(chat.getNombreChat())){
            return new Response(error, 404, "El chat no existe");

        }
        if(!chatRepository.existsById(chat.getId_chat())){
            return new Response(error, 404, "El chat no existe");

        }

       if(!(mensajeRepository.deleteAllByIdChat(chat.getId_chat())>0)){

        return new Response(error,500,"Error eliminando los mensajes del chat" );

       }
        chatRepository.delete(chat);
        return new Response(success, 200, "Chat eliminado correctamente");
    }

    public Response guardarMensaje(Mensaje mensaje){
        if(!chatRepository.existsById(mensaje.getId_chat().getId_chat())){
            return new Response(error, 404, "El chat no existe");

        }
        mensajeRepository.save(mensaje);
        return new Response(success, 200, "Mensaje guardado correctamente");
    }

    public List<MensajeDTO>obtenerMensajesDeChat(Long idChat){
        if(!chatRepository.existsById(idChat)){
            return null;
        }
        return mensajeRepository.findAllByChatId(idChat);
    }
    public List<ChatDTO> obtenerChatsDeUsuario(Long idUsuario){
        if(!usuarioRepository.existsById(idUsuario)){
            return null;
        }
        return chatRepository.findAllByIdUsuario(idUsuario);


    }
    public List<ChatDTO> obtenerChatsDeUsuarioCurrent(String token){
        Long idUsuario = jwtService.extractId(token);
        if(!usuarioRepository.existsById(idUsuario)){
            return null;
        }
        return chatRepository.findAllByIdUsuario(idUsuario);


    }
    public MensajeDTO ultimoMensaje(Long idChat){
        if(!chatRepository.existsById(idChat)){
            return null;
        }
return mensajeRepository.ultimoMensajeDeChat(idChat);

    }






}
