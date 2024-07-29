package com.likelion.scul.auth.kakao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoRequest {
    private String clientId;    // 애플리케이션의 클라이언트 ID
    private String redirectUri; // Google 로그인 후 redirect 위치
    private String code;    // 인가 코드
    private String grantType; // Authorization_code
}
