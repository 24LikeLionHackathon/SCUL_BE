package com.likelion.scul.club;

import com.likelion.scul.club.dto.ClubRequest;
import com.likelion.scul.club.dto.ClubResponse;
import com.likelion.scul.club.dto.ClubUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ClubController {

    public final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    // id에 해당하는 club 조회
    @GetMapping("/club/{id}")
    public ClubResponse getClub(@PathVariable Long id) {
        return clubService.findById(id);
    }

    // club 생성
    @PostMapping("/club")
    public ResponseEntity<ClubResponse> createClub(@RequestBody ClubRequest clubRequest) {
        // 필수 입력 사항 정리 필요
//        if (... == null) {
//            return ResponseEntity.badRequest().build();
//        }

        ClubResponse clubResponse = clubService.save(clubRequest);
        return ResponseEntity.created(URI.create("/club/" + clubResponse.getClubId())).body(clubResponse);
    }

    @PutMapping("/club/{id}")
    public ClubResponse updateClub(@RequestBody ClubUpdateRequest clubUpdateRequest, @PathVariable Long id) {
        return clubService.update(id, clubUpdateRequest);
    }


    @DeleteMapping("/club/{id}")
    public ResponseEntity<ClubResponse> deleteClub(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
