package com.example.ws.config;

import com.example.ws.handler.EchoWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.net.http.WebSocket;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws/echo")
                //CORS 설정
                .setAllowedOrigins("*");
                //.withSockJS();
                // default taskScheduler사용
                //.setTaskScheduler(myScheduler());
    }

    @Bean
    public WebSocketHandler myHandler(){
        return new EchoWebSocketHandler();
    }
}
