package com.likelion.scul.club;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.club.dto.*;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class ClubController {

    public final ClubService clubService;
    private final UserService userService;

    @Autowired
    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    // id에 해당하는 club 조회 (club 상세 조회)
    @GetMapping("/api/club/{id}")
    public ClubDetailResponse getClub(@PathVariable Long id, @RequestParam (value = "userId", required = false) Long userId ) {
        User user = (userId != null) ? userService.findById(userId).orElse(null) : null;

        return clubService.findById(id, user);
    }

    // sports에 해당하는 모든 club 조회
    @GetMapping("/api/club/sports/{id}")
    public List<ClubResponse> getClubList(@PathVariable Long id) {
        return clubService.findBySportsId(id);
    }

    // login
    // club 생성
    @PostMapping("/club")
    public ResponseEntity<ClubResponse> createClub(@ModelAttribute ClubRequest clubRequest, HttpServletRequest request) throws IOException {
        // 필수 입력 사항 정리 필요
//        if (... == null) {
//            return ResponseEntity.badRequest().build();
//        }
        System.out.println("clubRequest.getSportsName() = " + clubRequest.getSportsName());
        System.out.println("clubRequest.getClubName() = " + clubRequest.getClubName());
        System.out.println("clubRequest.getClubDate() = " + clubRequest.getClubDate());
        System.out.println(clubRequest.getClubContent());
        User user = (User) request.getAttribute("user");
        ClubResponse clubResponse = clubService.save(clubRequest, user);
        return ResponseEntity.created(URI.create("/club/" + clubResponse.getClubId())).body(clubResponse);
    }

    // login
    // club 수정
    @PutMapping("/club/{id}")
    public ClubResponse updateClub(@ModelAttribute ClubUpdateRequest clubUpdateRequest, @PathVariable Long id) throws IOException {
        return clubService.update(id, clubUpdateRequest);
    }

    // login
    // id에 해당하는 club 삭제
    @DeleteMapping("/club/{id}")
    public ResponseEntity<ClubResponse> deleteClub(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // login
    // 모집 완료로 상태 바꾸기
    @PatchMapping("/club/status/{id}")
    public ClubResponse completeClubRecruitment(@PathVariable Long id) {
        return clubService.updateClubStatus(id);
    }

    // club 필터 & 검색
//    @PostMapping("/api/club/sports/search/{id}")
//    public List<ClubResponse> filterClubs(@PathVariable Long id, @RequestBody ClubSearchRequest clubSearchRequest) {
//        return clubService.findBySearchOptions(id, clubSearchRequest);
//    }

    @PostMapping("/api/club/sports/search/{id}")
    public ClubSearchWithPageResponse filterClubs(@PathVariable Long id, @RequestBody ClubSearchWithPageRequest clubSearchWithPageRequest) {
        return clubService.findBySearchOptionsWithPage(id, clubSearchWithPageRequest);
    }

    // login
    // club 신청
    @PostMapping("/club/application/{id}")
    public ResponseEntity<ClubApplicationResponse> applicateClub(@PathVariable Long id, @RequestBody ClubApplicationRequest clubApplicationRequest, HttpServletRequest request) {
        User applicant = (User) request.getAttribute("user");
        // 이미 신청을 한 경우에는 bad request
        if (clubService.getApplicationByClubAndApplicant(id, applicant) != null) {
            return ResponseEntity.badRequest().build();
        }
        ClubApplicationResponse clubApplicationResponse = clubService.saveApplication(id, clubApplicationRequest, applicant);
        return ResponseEntity.created(URI.create("/club/application/" + clubApplicationResponse.getClubApplicationId())).body(clubApplicationResponse);
    }

    // login
    // club 신청 승인
    @PostMapping("/club/application/approve/{id}")
    public ResponseEntity<ClubApplicationResponse> approveApplication(@PathVariable Long id, @RequestBody ClubApplicationApproveRequest clubApplicationApproveRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        ClubApplicationResponse clubApplicationResponse = clubService.approveApplication(id, clubApplicationApproveRequest, user);
        return ResponseEntity.ok(clubApplicationResponse);
    }

    // login
    // 내 소모임 조회
    @GetMapping("/club/mine")
    public ResponseEntity<List<ClubResponse>> getMyClubs(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(clubService.findMyClub(user));
    }
}

