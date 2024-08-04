package com.likelion.scul.club;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.club.dto.ClubRequest;
import com.likelion.scul.club.dto.ClubResponse;
import com.likelion.scul.club.dto.ClubUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    private ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    // club 생성
    public ClubResponse save(ClubRequest clubRequest) {
        String status = "모집 중";
        Club club = new Club(clubRequest, status);

        clubRepository.save(club);

        return ClubResponse.toClubResponse(club);
    }

    // id에 해당하는 club 조회
    public ClubResponse findById(Long id) {
        return ClubResponse.toClubResponse(clubRepository.findByClubId(id));
    }

    // sports의 모든 club 조회

    // club 수정
    public ClubResponse update(Long id, ClubUpdateRequest request) {
        Club club = clubRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("id에 해당하는 Club을 찾을 수 없습니다");
        });

        club.update(request.getClubName(), request.getClubContent(), request.getClubPlace(), request.getClubDate(), request.getClubTotalNumber(), request.getClubParticipateNumber(), request.getClubQnaLink(), request.getClubParticipateLink(), request.getClubCost(), request.getClubImage(), request.getClubStatus());

        return ClubResponse.toClubResponse(club);
    }

    // id에 해당하는 club 삭제
    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }
}
