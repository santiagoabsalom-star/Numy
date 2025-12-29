package com.surrogate.numy.models.DTO;

public record MensajeDTO(Long idMensaje, Long idChat, String nombreChat, String contenido,String emisor, String receptor) {
}
