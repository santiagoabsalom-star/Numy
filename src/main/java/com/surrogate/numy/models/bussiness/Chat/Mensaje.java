package com.surrogate.numy.models.bussiness.Chat;

import com.surrogate.numy.models.bussiness.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje", nullable = false, unique = true)
    private Long id_mensaje;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_chat", nullable = false)
    private Chat id_chat;
    @Column(name = "contenido", nullable = false)
    private String contenido;
    @Column(name="fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @JoinColumn(name="id_emisor", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario emisor;
    @JoinColumn(name = "id_receptor", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario receptor;


}
