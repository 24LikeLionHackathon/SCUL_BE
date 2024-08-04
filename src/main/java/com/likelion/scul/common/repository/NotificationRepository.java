package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserUserId(Long userId);

    @Query(value = "select * " +
            "from scul.notification " +
            "where user_id = :userId and is_Read = 1", nativeQuery = true)
    List<Notification> findReadNotificationByUserId(Long userId);

    @Query(value = "select * " +
            "from scul.notification " +
            "WHERE user_id = :userId and is_Read = 0", nativeQuery = true)
    List<Notification> findUnReadNotificationByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE scul.notification " +
            "SET is_Read = 1 WHERE notification_id = :notificationId;", nativeQuery = true)
    void readNotification(Long notificationId);

    @Query(value = "SELECT count(*) from scul.notification " +
            "WHERE user_id = :userId and is_Read = 0", nativeQuery = true)
    int getUnReadCountByUserId(Long userId);

    @Query(value = "SELECT count(*) from scul.notification " +
            "WHERE user_id = :userId and is_Read = 1", nativeQuery = true)
    int getReadCountByUserId(Long userId);

    @Query(value = "SELECT count(*) from scul.notification " +
            "WHERE user_id = :userId", nativeQuery = true)
    int getCountByUserId(Long userId);

}
