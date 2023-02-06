package com.acmcsuf.triggers;
import java.sql.SQLException;

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

        // TODO: Add authentication process (JWT?)

        // TODO: Add validation for user_id

        // Sync data from database
        try {
            Database.syncUserData(userId, Trigger.triggerMap, Trigger.triggerToggle);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error syncing data for user_id: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Data synced successfully for user_id: " + userId, HttpStatus.OK);
    }
    
}