package com.example.ws.app.echo.repository;

import com.example.ws.app.echo.dto.EchoPayloadDTO;
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
    private MultiValueMap<String, EchoPayloadDTO> messageCacheMap = new LinkedMultiValueMap<>();
    private static int MAX_MESSAGE_COUNT_PER_TOPIC = 10;

    public void push(String season, EchoPayloadDTO message){
        messageCacheMap.add(season,message);
        List<EchoPayloadDTO> messages =  messageCacheMap.get(season);
        if(messages.size() > MAX_MESSAGE_COUNT_PER_TOPIC){
            messages.remove(0);
        }
    }

    public List<EchoPayloadDTO> getAllMessage(String season){
        return Collections.unmodifiableList(messageCacheMap.getOrDefault(season, Collections.EMPTY_LIST));
    }
}
