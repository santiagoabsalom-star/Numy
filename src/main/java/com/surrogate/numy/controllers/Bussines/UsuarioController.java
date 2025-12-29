package com.surrogate.numy.controllers.Bussines;


import com.surrogate.numy.models.DTO.UsuarioDto;
import com.surrogate.numy.models.bussiness.Usuario;
import com.surrogate.numy.models.peticiones.Response;
import com.surrogate.numy.services.auth.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/updateCurrentUser")
    public ResponseEntity<Response> updateCurrentUser(HttpServletRequest request, Usuario usuario) {

        String token = getTokenFromRequest(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Response response = usuarioService.updateCurrentUser(token, usuario);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Response> eliminarUsuario(@RequestParam Long id_usuario) {
        Response response = usuarioService.eliminar(id_usuario);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getUsuarios")
    public ResponseEntity<List<UsuarioDto>> getUsuarios(@RequestParam Long id_usuario) {
        List<UsuarioDto> usuarioDtos = usuarioService.findAllWithLimit(id_usuario);
        if (usuarioDtos == null || usuarioDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioDtos);
    }

    @GetMapping("/getUsuarioById")
    public ResponseEntity<UsuarioDto> getUsuarioById(@RequestParam Long id_usuario) {
        UsuarioDto usuarioDto = usuarioService.findDTOById(id_usuario);
        if (usuarioDto == null) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(usuarioDto);
    }

    @GetMapping("/get5Usuarios")
    public ResponseEntity<List<UsuarioDto>> get5Usuarios() {
        List<UsuarioDto> usuarioDtos = usuarioService.find5DTOS();
        if (usuarioDtos == null) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(usuarioDtos);


    }
    @GetMapping("/getUsuarioByIdInstitucion")
    public ResponseEntity<UsuarioDto> getUsuarioByIdInstitucion(@RequestParam Long id_institucion) {
        UsuarioDto usuarioDto = usuarioService.findDTOByIdInstitucion(id_institucion);
        if (usuarioDto == null) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(usuarioDto);
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length == 0) {

            return null;
        }

        return Objects.requireNonNull(Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("AUTH_TOKEN")).findFirst().orElse(null)).getValue();
    }
}
