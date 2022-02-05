package com.example.ws.app.echo.repository;

import com.example.ws.app.echo.dto.EchoMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

/**
 * Topic별 메시지들을 Cache해놓음.
 */
@Repository
public class EchoMemoryRepository {
    //구조 : topic, List<>
    private MultiValueMap<String,EchoMessageDTO> messageCacheMap = new LinkedMultiValueMap<>();
    private static int MAX_MESSAGE_COUNT_PER_TOPIC = 10;

    public void push(EchoMessageDTO message){
        String key = message.getSeason();
        messageCacheMap.add(key,message);
        List<EchoMessageDTO> messages =  messageCacheMap.get(key);
        if(messages.size() > MAX_MESSAGE_COUNT_PER_TOPIC){
            messages.remove(0);
        }
    }

    public List<EchoMessageDTO> getAllMessage(String season){
        return Collections.unmodifiableList(messageCacheMap.get(season));
    }
}
