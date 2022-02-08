package com.example.ws.app.echo.service;

import com.example.ws.app.echo.dto.EchoPayloadDTO;
import com.example.ws.app.echo.repository.EchoMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableScheduling
@Service
@AllArgsConstructor
@Slf4j
public class MessagePushService {

    private final EchoMemoryRepository repository;
    private final SimpMessagingTemplate template;

    @Scheduled(fixedRate = 5000)
    public void sendMessage(){
        List<EchoPayloadDTO> springMessages =  repository.getAllMessage("spring");
        List<EchoPayloadDTO> summerMessages =  repository.getAllMessage("summer");
        List<EchoPayloadDTO> autumnMessages =  repository.getAllMessage("autumn");
        List<EchoPayloadDTO> winterMessages =  repository.getAllMessage("winter");
        log.info("MessagePushService.sendMessage 실행됨.");

        template.convertAndSend("/topic/season/spring",springMessages);
        template.convertAndSend("/topic/season/summer",summerMessages);
        template.convertAndSend("/topic/season/autumn",autumnMessages);
        template.convertAndSend("/topic/season/winter",winterMessages);
    }
}
