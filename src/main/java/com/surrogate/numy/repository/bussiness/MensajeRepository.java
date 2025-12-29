package com.surrogate.numy.repository.bussiness;

import com.surrogate.numy.models.DTO.MensajeDTO;
import com.surrogate.numy.models.bussiness.Chat.Mensaje;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {



    @Modifying
    @Query("delete from Mensaje m where m.id_chat.id_chat= :idChat")
   @BatchSize(size =50)
    int deleteAllByIdChat(Long idChat);
@Query("Select new com.surrogate.numy.models.DTO.MensajeDTO(m.id_mensaje, m.id_chat.id_chat, m.id_chat.nombreChat, m.contenido, m.emisor.nombre, m.receptor.nombre) from Mensaje m where m.id_chat.id_chat= :idChat order by m.id_mensaje asc")
    List<MensajeDTO> findAllByChatId(Long idChat);
    @Query("Select new com.surrogate.numy.models.DTO.MensajeDTO(m.id_mensaje, m.id_chat.id_chat, m.id_chat.nombreChat, m.contenido, m.emisor.nombre, m.receptor.nombre) from Mensaje m where m.id_chat.id_chat= :idChat order by m.id_mensaje asc")
//TODO CAMIBAR METODOS PQ NO TIENEN SENTIDO
    MensajeDTO ultimoMensajeDeChat(Long idChat);
}
