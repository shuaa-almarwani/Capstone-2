package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }





    // #19
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }


}