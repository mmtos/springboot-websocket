package com.example.ws.app.echo.controller;

import com.example.ws.app.echo.dto.EchoMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * STOMP기반 EchoApp에서 메시지를 전달을 위한 핸들러
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class EchoAppController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/enter")
    public void doSubscribe(EchoMessageDTO message){
        //TODO : Session Id 받아서 표시하기.
        String topic = message.getSeason();
        log.info(message.toString());
        String fullMessage = topic + "(by new subscriber," + message.getSessionId() + ") : " + message.getMessage();
        template.convertAndSend("/topic/season/" + topic, fullMessage);
    }

    @MessageMapping("/sendMessage")
    public void submitEchoMessage(EchoMessageDTO message){
        // 이미 subscribe를 완료한 client에서 전달하는 추가 메세지
        String topic = message.getSeason();
        String fullMessage = topic + "(" + message.getSessionId() + ") : " + message.getMessage();
        template.convertAndSend("/topic/season/" + topic, fullMessage);
    }
}
