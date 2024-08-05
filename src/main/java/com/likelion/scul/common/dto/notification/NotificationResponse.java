package com.likelion.scul.common.dto.notification;

import com.likelion.scul.common.domain.Notification;
import lombok.Data;

import java.util.List;

@Data
public class NotificationResponse {
    private List<Notification> notifications;
    private int notificationCount;
}
