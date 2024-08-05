package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.Notification;
import com.likelion.scul.common.dto.notification.NotificationRequest;
import com.likelion.scul.common.dto.notification.NotificationResponse;
import com.likelion.scul.common.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;
    private UserService userService;

    public NotificationController(
            NotificationService notificationService,
            UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/all/{pageNum}")
    public ResponseEntity<NotificationResponse> getNotifications(@PathVariable(value = "pageNum") int pageNum, HttpServletRequest request) {
        Long userId = userService.extractUserIdByAccessToken(request);

        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getCount(userId));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/read/{pageNum}")
    public ResponseEntity<NotificationResponse> getReadNotifications(@PathVariable(value = "pageNum") int pageNum, HttpServletRequest request) {
        Long userId = userService.extractUserIdByAccessToken(request);

        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getReadNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getReadCount(userId));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread/{pageNum}")
    public ResponseEntity<NotificationResponse> getUnReadNotifications(@PathVariable(value = "pageNum") int pageNum, HttpServletRequest request) {
        Long userId = userService.extractUserIdByAccessToken(request);

        NotificationResponse response = new NotificationResponse();
        response.setNotifications(notificationService.getUnReadNotifications(userId, pageNum));
        response.setNotificationCount(notificationService.getUnReadCount(userId));

        return ResponseEntity.ok(response);
    }

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