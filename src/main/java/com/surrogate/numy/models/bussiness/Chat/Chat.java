package com.surrogate.numy.models.bussiness.Chat;

import com.surrogate.numy.models.bussiness.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat", uniqueConstraints = @UniqueConstraint(columnNames = "nombre_chat"))
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat", nullable = false, unique = true)
    private Long id_chat;
    @Column(name="nombre_chat", nullable = false)
    private String nombreChat;
    @JoinColumn(name="id_usuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @JoinColumn(name="id_administrador", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario administrador;


}
