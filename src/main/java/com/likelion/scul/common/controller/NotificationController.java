package com.likelion.scul.common.controller;

import com.likelion.scul.common.domain.Notification;
import com.likelion.scul.common.dto.notification.NotificationRequest;
import com.likelion.scul.common.dto.notification.NotificationResponse;
import com.likelion.scul.common.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all/{userId}/{pageNum}")
    public ResponseEntity<NotificationResponse> getNotifications(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageNum") int pageNum) {
        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getCount(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read/{userId}/{pageNum}")
    public ResponseEntity<NotificationResponse> getReadNotifications(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageNum") int pageNum) {
        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getReadNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getReadCount(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread/{userId}/{pageNum}")
    public ResponseEntity<NotificationResponse> getUnReadNotifications(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageNum") int pageNum) {
        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getUnReadNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getUnReadCount(userId));
        return ResponseEntity.ok(response);
    }

    // 테스트 통과
    @PostMapping("/test")
    public ResponseEntity<Notification> saveNotification(@RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.saveNotification(request));
    }

    @PostMapping
    public ResponseEntity<Void> readNotification(@RequestParam Long notificationId) {
        notificationService.readNotification(notificationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotification(@RequestParam Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
