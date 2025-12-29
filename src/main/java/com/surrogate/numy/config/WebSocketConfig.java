package com.surrogate.numy.config;

import com.surrogate.numy.websocket.ChatWebSocketHandler;
import com.surrogate.numy.websocket.GameWebSocketHandler;
import com.surrogate.numy.websocket.InterceptorJWT;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final InterceptorJWT interceptorJWT;
    private final GameWebSocketHandler gameWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat/**")
                .addHandler(gameWebSocketHandler, "/game/**")
                .addInterceptors(interceptorJWT)
                .setAllowedOrigins("*");


    }
}
