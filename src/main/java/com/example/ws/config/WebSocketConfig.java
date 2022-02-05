package com.example.ws.config;

import com.example.ws.handler.EchoWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.http.WebSocket;
import java.util.Map;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws/echo")
                //CORS 설정
                .setAllowedOrigins("*")
                .addInterceptors(new MyHandshakeInterceptor());
                //.withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler(){
        return new EchoWebSocketHandler();
    }

    class MyHandshakeInterceptor implements HandshakeInterceptor{

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            log.info("MyHandshakeInterceptor.beforeHandshake()");
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            log.info("MyHandshakeInterceptor.afterHandshake() 작동");
        }
    }
}
