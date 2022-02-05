package com.example.ws;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/home")
    public String index(){
        return "index";
    }

    @GetMapping("/echo")
    public String echo(){
        return "stompEchoApp";
    }
}
