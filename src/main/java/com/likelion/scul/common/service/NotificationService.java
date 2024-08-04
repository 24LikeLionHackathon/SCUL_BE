package com.likelion.scul.common.service;

import com.likelion.scul.common.domain.Notification;
import com.likelion.scul.common.dto.notification.NotificationRequest;
import com.likelion.scul.common.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final static int PAGE_NOTIFICATION_NUMBER = 5;
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(NotificationRequest request) {
        return notificationRepository.save(makeNewNotification(request));
    }
    
    private Notification makeNewNotification(
            NotificationRequest request
    ){
        Notification notification = new Notification();
        notification.setUser(request.getUser());
        notification.setNotificationType(request.getType());
        notification.setNotificationTime(request.getTime());
        notification.setNotificationName(request.getName());
        notification.setContent(request.getContent());
        notification.setBoardName(request.getBoardName());
        notification.setTag(request.getTag());
        notification.setContentName(request.getContentName());
        notification.setUrl(request.getUrl());

        return notification;
    }

    public List<Notification> getNotifications(Long userId,int pageNum) {
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        return paging(notifications,pageNum);
    }

    public List<Notification> getReadNotifications(Long userId, int pageNum) {
        List<Notification> notifications = notificationRepository.findReadNotificationByUserId(userId);
        return paging(notifications,pageNum);
    }

    public List<Notification> getUnReadNotifications(Long userId, int pageNum) {
        List<Notification> notifications = notificationRepository.findUnReadNotificationByUserId(userId);
        return paging(notifications,pageNum);
    }

    public int getUnReadCount(Long userId) {
        return notificationRepository.getUnReadCountByUserId(userId);
    }

    public int getReadCount(Long userId) {
        return notificationRepository.getReadCountByUserId(userId);
    }

    public int getCount(Long userId) {
        return notificationRepository.getCountByUserId(userId);
    }

    public void readNotification(Long notificationId) {
        notificationRepository.readNotification(notificationId);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private List<Notification> paging(List<Notification> notifications, int pageNum) {
        if ( notifications.size() < PAGE_NOTIFICATION_NUMBER ) {
            return notifications;
        }
        int from = pageNum * PAGE_NOTIFICATION_NUMBER - PAGE_NOTIFICATION_NUMBER ;
        int to ;
        if ( (int) (notifications.size() / PAGE_NOTIFICATION_NUMBER) < pageNum ){
            to = notifications.size() ;
        }
        else {
            to = pageNum * PAGE_NOTIFICATION_NUMBER ;
        }
        return notifications.subList(from, to);
    }
}
