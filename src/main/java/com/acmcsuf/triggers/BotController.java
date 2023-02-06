package com.acmcsuf.triggers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {

    @GetMapping("/api/ping")
    public String ping() {
        return "Hello, world!";
    }
    
}