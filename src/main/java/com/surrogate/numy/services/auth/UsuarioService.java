package com.surrogate.numy.services.auth;

import com.surrogate.numy.models.DTO.UsuarioDto;
import com.surrogate.numy.models.bussiness.Chat.Chat;
import com.surrogate.numy.models.bussiness.Usuario;
import com.surrogate.numy.models.login.UserPrincipal;
import com.surrogate.numy.models.peticiones.Response;
import com.surrogate.numy.repository.bussiness.ChatRepository;
import com.surrogate.numy.repository.bussiness.UsuarioRepository;
import com.surrogate.numy.services.auth.JWT.JWTService;
import com.surrogate.numy.services.bussiness.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
//todo: completar metodos de busqueda
    private final UsuarioRepository usuarioRepository;
    private final JWTService jwtService;
    private final String success;
    private final String error;
    private final ChatRepository chatRepository;
    private final ChatService chatService;


    @Transactional(readOnly = true)
    public List<UsuarioDto> findAvailableUsersInitialized() {

return new ArrayList<>();    }

    public Optional<UsuarioDto> me(String token) {

        if (token == null) {
            return Optional.empty();
        }
//return usuarioRepository.getUserById(jwtService.extractId(token));
return Optional.empty();
    }

    public List<UsuarioDto> findAllByEmail() {

return new ArrayList<>();    }

    public Optional<Usuario> findById(Long uid) {
        return usuarioRepository.findById(uid);
    }
    @Transactional
    public Response eliminar(Long id_usuario) {
        if (usuarioRepository.existsById(id_usuario)) {
            usuarioRepository.deleteById(id_usuario);
            return new Response("El usuario se ha eliminado correctamente", 200, "Success");
        }
        return new Response("El usuario no existe", 404, "Error");

    }

    public UsuarioDto findDTOById(Long idUsuario) {
return new UsuarioDto(1L,"h","ro","e","e","e");    }

    public List<UsuarioDto> findAllWithLimit(Long idUsuario) {
return new ArrayList<>();    }

    public List<UsuarioDto> find5DTOS() {
return new ArrayList<>()  ;  }
    @Transactional
    public Response updateCurrentUser(String token, Usuario usuario) {


        Usuario usuarioExistente = usuarioRepository.findById(getIdFromSecurityContext()).orElse(null);

        if (usuarioExistente == null) {
            return new Response(error, 404, "Usuario no encontrado");
        }


        if (usuario.getNombre() != null) {
            if (chatRepository.existByNombreLike(usuarioExistente.getNombre())) {

                log.info(usuarioExistente.getNombre());
                log.info("El usuario tiene chats asociados, se cambiara el nombre en los chats");
                cambiarNombreChat(usuarioExistente.getNombre(), usuario.getNombre());
            }

            usuarioExistente.setNombre(usuario.getNombre());
            jwtService.InvalidateToken(token);

        }
        if (usuario.getEmail() != null) {
            usuarioExistente.setEmail(usuario.getEmail());
        }
        if (usuario.getRol() != null) {
            return new Response(error, 403, "No puedes camibiar el rol del usuario");
        }
        if (usuario.getBiografia() != null) {
            usuarioExistente.setBiografia(usuario.getBiografia());
        }
        if (usuario.getImagenUrl() != null) {
            usuarioExistente.setImagenUrl(usuario.getImagenUrl());
        }


        //usuarioRepository.actualizarUsuarioCurrent(usuarioExistente);


        return new Response(success, 200, "Usuario actualizado correctamente");
       }

    public UsuarioDto findDTOByIdInstitucion(Long id_institucion) {
        return new UsuarioDto(1L,"h","ro","e","e","e");    }


    private Long getIdFromSecurityContext(){

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userPrincipal.getId();
    }
    private String getEmailFromSecurityContext(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userPrincipal.getEmail();
    }
    private void cambiarNombreChat(String nombreAntiguo, String nombreNuevo) {
        if (!chatRepository.existsByNombreChat(nombreAntiguo)) {
            return;
        }


        List<Chat> chats = chatRepository.findBynombreChatLike(nombreAntiguo);
        if (chats.isEmpty()) {
            return;
        }
        for (Chat chat : chats) {
            String[] partes = nombreNuevo.split("_");
            if (partes.length != 2) {
                return;
            }
            if (!chat.getUsuario().getNombre().equals(partes[0]) || !chat.getAdministrador().getNombre().equals(partes[1])) {
                return;
            }
            if (chat.getUsuario().getNombre().equals(partes[0])) {
                chat.setNombreChat(nombreNuevo + "_" + chat.getAdministrador().getNombre());
            } else {
                chat.setNombreChat(chat.getUsuario().getNombre() + "_" + nombreNuevo);
            }
            chatRepository.save(chat);
        }
    }}
