package com.likelion.scul.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.scul.auth.domain.KakaoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {
    @Value("${kakao.client.key}")
    private String kakaoClientKey;
    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;
    @Value("${kakao.auth.url}")
    private String kakaoAuthUrl;
    @Value("${kakao.user.api.url}")
    private String kakaoUserApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public KakaoToken getToken(String authCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientKey);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", authCode);

        KakaoToken kakaoToken = restTemplate.postForObject(
                kakaoAuthUrl,
                new HttpEntity<>(body, headers),
                KakaoToken.class);

        return kakaoToken;
    }

    public String getEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("property_keys", "[\"kakao_account.email\"]");

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // POST 요청 보내기
        ResponseEntity<String> userApiResponse = restTemplate.postForEntity(kakaoUserApiUrl, request, String.class);

        try {
            String email = parseEmailFrom(userApiResponse.getBody());
            return email;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json이 비어있습니다");
        }
    }

    private String parseEmailFrom(String JsonBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 문자열을 JsonNode로 파싱
        JsonNode rootNode = objectMapper.readTree(JsonBody);

        // email 값 추출
        JsonNode email = rootNode.path("kakao_account").path("email");

        return email.asText();
    }


}
