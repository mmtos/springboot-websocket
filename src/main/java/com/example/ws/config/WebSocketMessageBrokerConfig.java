package com.example.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/stomp/echo")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.configureBrokerChannel(); // application -> message broker로 가는 채널을 설정하기 위한 ChannelRegistration 리턴
        registry.setApplicationDestinationPrefixes("/echoApp")
                .setPreservePublishOrder(false) //false인 경우 메시지 발행 순서를 지켜서 client에게 전송됨
                .setCacheLimit(1024) //현재는  destination cache 개수를 조절하는데 사용. 기본값으로 캐시되는 destination (with sessionid, subscription id)의 수는 1024개
                .enableSimpleBroker("/topic")
                //simple Broker 설정
                // The first number represents how often the server will write or send a heartbeat.
                // The second is how often the client should write.
                // 0 means no heartbeats.
                // 기본값은 0,0 이나 setTaskScheduler를 설정할 경우  "10000, 10000"이 기본값이 된다.
                .setHeartbeatValue(new long[]{0L,0L})
                //Setting this property also sets the heartbeatValue to "10000, 10000".
                //.setTaskScheduler()
                // 아직 이해 X
                //.setSelectorHeaderName()
                ;

    }

    // 웹소켓 메시지 전송 관련 설정 (+ STOMP 메시지 처리(processing) 관련 설정)
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry
                //STOMP frame 최대 크기 설정 메소드. 웹소켓 자체의 메시지길이 limit를 설정하는 건 아님
                .setMessageSizeLimit(64*1024) // 기본값 64K ,  웹소켓 메시지의 길이보다 STOMP frame가 길어도 상관없다. 여러 웹소켓메시지로 나눠서 받을 수도 있다.
                // 1분안에 the first sub-protocol message(Stomp frame) 가 서버로 전달되지 않으면 세션을 종료한다.
                .setTimeToFirstMessage(60000) // 기본값 1분
                // 메시지가 client에게 sending되기까지의 시간을 제한.
                // blocking IO(such as the BIO connector in Tomcat)를 사용한다면  웹소켓세션은 종료되도 TCP Connection이 종료되지 않을 수도 있다.
                // 왠만하면 NIO(Tomcat's NIO connector used by default on Tomcat 8)를 쓰고 부득이한 경우 OS-level TCP setting이 필요하다
                .setSendTimeLimit(10 * 1000) // 기본값 10초
                // 메시지가 안나가면 그만큼 버퍼에도 쌓인다. 버퍼의 최대값에 다다르면 세션종료를 시도한다.
                .setSendBufferSizeLimit(512 * 1024) // 기본값 512 * K * Byte
                // for advanced use case.
                //.addDecoratorFactory()
        ;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // inbound 채널 설정
        ThreadPoolTaskExecutor myThreadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        myThreadPoolTaskExecutor.setCorePoolSize(10);
        registration
                // production level에서는 최적화를 위해 설정 필요
                .taskExecutor(myThreadPoolTaskExecutor)
                .keepAliveSeconds(60)//기본값 60초
                .maxPoolSize(Integer.MAX_VALUE)
        ;
    }
}
