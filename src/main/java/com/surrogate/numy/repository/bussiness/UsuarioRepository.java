package com.surrogate.numy.repository.bussiness;

import com.surrogate.numy.models.DTO.UsuarioDto;
import com.surrogate.numy.models.bussiness.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Usuario findUsuarioByNombre(String username);
    boolean existsByNombre(String nombre);

    Usuario findByNombre(String emisor);
}