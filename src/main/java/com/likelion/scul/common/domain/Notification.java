package com.likelion.scul.common.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    int notificationType; // 1 : 소모임 신청 알림, 2 : 소모임 수락되었음 , 3 : 소모임 거절되었음 , 4 : 게시물에 댓글 달림 등등
    String notificationName; // 알림 제목
    String notificationTime;
    String content;// 알림 내용
    String boardName; // 게시판 이름
    String tag; // 게시판 태그
    String contentName; // 알림 발생 지점 제목
    String url; // 해당 지점으로 이동할 수 있는 URL
    boolean isRead = false;

}