package com.surrogate.numy.repository.bussiness;

import com.surrogate.numy.models.DTO.ChatDTO;
import com.surrogate.numy.models.bussiness.Chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    @Query("Select count(*) > 0 from Chat c where c.nombreChat = :nombreChat")
    List<ChatDTO> findAllByIdUsuario(Long idUsuario);

    @Query("Select count(*) > 0 from Chat c where c.nombreChat = :nombreChat")
    boolean existsByNombreChat(String nombreChat);
    @Query("Select count(*) > 0 from Chat c where c.nombreChat like :nombreLike")
    boolean existByNombreLike(String nombreLike);
    Chat findBynombreChat(String nombreChat);
    @Query("Select c from Chat c where c.nombreChat like %:nombreLike%")
    List<Chat> findBynombreChatLike(String nombreLike);
//TODO CAMBIAR EL PRIMER METODO
}
