package com.likelion.scul.common.dto.notification;

import com.likelion.scul.common.domain.User;
import lombok.Data;

@Data
public class NotificationRequest {
    User user;
    int type;
    String name;
    String time;
    String content;// 알림 내용
    String boardName; // 게시판 이름
    String tag; // 게시판 태그
    String contentName; // 알림 발생 지점 제목
    String url;
}
