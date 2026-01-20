package com.surrogate.numy.repository.bussiness;

import com.surrogate.numy.models.DTO.ConexionDTO;
import com.surrogate.numy.models.bussiness.Conexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Long> {
//WHY IS THIS FUCKIN QUERY RETURNING NULL IF IN THE MARIADB CONSOLE RETURNING ALL THIS SHIT
    @Query("Select case when count(c)> 0 then true else false end from Conexion c where c.id_usuario1.nombre = :nombreUsuario or c.id_usuario2.nombre = :nombreUsuario" )
    boolean existsConexionByNombreUsuario(String nombreUsuario);
    @Query("SELECT COUNT(c) FROM Conexion c WHERE c.id_usuario1.nombre = :nombre OR c.id_usuario2.nombre = :nombre")
    Long countByNombre(@Param("nombre") String nombre);
    @Query("SELECT c.id_conexion AS idConexion, " +
            "u1.nombre AS nombreUsuario1, " +
            "u2.nombre AS nombreUsuario2 " +
            "FROM Conexion c " +
            "JOIN c.id_usuario1 u1 " +
            "JOIN c.id_usuario2 u2 " +
            "WHERE u1.nombre = :nombreUsuario OR u2.nombre = :nombreUsuario")
    ConexionDTO findConexion(@Param("nombreUsuario") String nombreUsuario);
    @Query("SELECT c.id_conexion FROM Conexion c " +
            "WHERE c.id_usuario1.nombre = :nombre OR c.id_usuario2.nombre = :nombre")
    Long findConexionId(@Param("nombre") String nombre);
}
