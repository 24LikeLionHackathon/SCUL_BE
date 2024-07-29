package com.likelion.scul.auth.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class KakaoToken {
    private String token_type;
    private String access_token;
    private String id_token;
    private String refresh_token;
    private Integer expires_in;
    private Integer refresh_token_expires_in;
    private String scope;
}
