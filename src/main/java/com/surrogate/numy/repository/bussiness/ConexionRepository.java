package com.surrogate.numy.repository.bussiness;

import com.surrogate.numy.models.DTO.ConexionDTO;
import com.surrogate.numy.models.bussiness.Conexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Long> {

    @Query("Select new com.surrogate.numy.models.DTO.ConexionDTO(c.id_conexion, c.id_usuario1.nombre, c.id_usuario2.nombre) from Conexion c where c.id_usuario1.nombre = :nombreUsuario or c.id_usuario2.nombre = :nombreUsuario" )
    ConexionDTO findConexionByNombreUsuario(String nombreUsuario);
    @Query("Select case when count(c)> 0 then true else false end from Conexion c where c.id_usuario1.nombre = :nombreUsuario or c.id_usuario2.nombre = :nombreUsuario" )
    boolean existsConexionByNombreUsuario(String nombreUsuario);

}
