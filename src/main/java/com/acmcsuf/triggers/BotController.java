package com.acmcsuf.triggers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {

    @GetMapping("/api/ping")
    public String ping() {
        return "Hello, world!";
    }

    @PostMapping("/api/sync")
    public ResponseEntity<String> sync(@RequestParam("user_id") String userId) {

        // TODO: Add sync logic here

        return new ResponseEntity<>("Data synced successfully for user_id: " + userId, HttpStatus.OK);
    }
    
}