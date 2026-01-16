package com.surrogate.numy.models.bussiness;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Conexion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conexion", nullable = false)
    private long id_conexion;
    @JoinColumn(name="id_usuario1", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario id_usuario1;
    @JoinColumn(name="id_usuario2", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario id_usuario2;

}
