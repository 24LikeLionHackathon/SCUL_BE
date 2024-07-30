package com.likelion.scul.club;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.club.dto.ClubRequest;
import com.likelion.scul.club.dto.ClubResponse;
import com.likelion.scul.club.dto.ClubUpdateRequest;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ClubController {

    public final ClubService clubService;
    private final UserService userService;

    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    // id에 해당하는 club 조회 (club 상세 조회)
    @GetMapping("/club/{id}")
    public ClubResponse getClub(@PathVariable Long id) {
        return clubService.findById(id);
    }

    // club 생성
    @PostMapping("/club")
    public ResponseEntity<ClubResponse> createClub(@RequestBody ClubRequest clubRequest, HttpServletRequest request) {
        // 필수 입력 사항 정리 필요
//        if (... == null) {
//            return ResponseEntity.badRequest().build();
//        }
        User user = (User) request.getAttribute("user");
        ClubResponse clubResponse = clubService.save(clubRequest, user);
        return ResponseEntity.created(URI.create("/club/" + clubResponse.getClubId())).body(clubResponse);
    }

    // club 수정
    @PutMapping("/club/{id}")
    public ClubResponse updateClub(@RequestBody ClubUpdateRequest clubUpdateRequest, @PathVariable Long id) {
        return clubService.update(id, clubUpdateRequest);
    }

    // id에 해당하는 club 삭제
    @DeleteMapping("/club/{id}")
    public ResponseEntity<ClubResponse> deleteClub(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

