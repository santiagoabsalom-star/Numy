package com.surrogate.numy.services.bussiness;

import com.surrogate.numy.models.DTO.ConexionDTO;
import com.surrogate.numy.models.bussiness.Conexion;
import com.surrogate.numy.repository.bussiness.ConexionRepository;
import com.surrogate.numy.repository.bussiness.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConexionService {
    private final UsuarioRepository usuarioRepository;
    private final ConexionRepository conexionRepository;
    public ConexionDTO getConexion(String nombreUsuario){
        ConexionDTO conexionDTO= conexionRepository.findConexion(nombreUsuario);
        if(conexionDTO==null){
            log.info("No se encontró conexión para el usuario: {}", nombreUsuario);
            return null;
        }else {
            log.info("Conexión encontrada para el usuario: {}", nombreUsuario);
            return conexionDTO;

        }}


public boolean existeConexion(String nombreUsuario){
        return conexionRepository.existsConexionByNombreUsuario(nombreUsuario);
    }
    public void guardar(Conexion conexion){
        conexionRepository.save(conexion);
    }
}
