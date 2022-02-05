package com.example.ws.app.echo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EchoMessageDTO {
    private String message;
    private String sessionId;
    private String season; //Topic에 해당

}
