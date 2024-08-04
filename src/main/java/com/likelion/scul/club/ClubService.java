package com.likelion.scul.club;

import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.ClubApplication;
import com.likelion.scul.club.dto.*;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.repository.SportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final SportsRepository sportsRepository;
    private final ClubRepositoryCustom clubRepositoryCustom;
    private final ClubApplicationRepository clubApplicationRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository, SportsRepository sportsRepository, ClubRepositoryCustom clubRepositoryCustom, ClubApplicationRepository clubApplicationRepository) {
        this.clubRepository = clubRepository;
        this.sportsRepository = sportsRepository;
        this.clubRepositoryCustom = clubRepositoryCustom;
        this.clubApplicationRepository = clubApplicationRepository;
    }

    // club 생성
    public ClubResponse save(ClubRequest clubRequest, User user) {
        String status = "모집 중";

        Sports sports = sportsRepository.getReferenceById(clubRequest.getSportsId());

        Club club = new Club(clubRequest, status, user, sports);

        clubRepository.save(club);

        return ClubResponse.toClubResponse(club);
    }

    // id에 해당하는 club 조회
    public ClubResponse findById(Long id) {
        return ClubResponse.toClubResponse(clubRepository.findByClubId(id));
    }

    // sports의 모든 club 조회
    public List<ClubResponse> findBySportsId(Long sportsId) {
        return clubRepository.findAllBySports_SportsIdOrderByCreatedAtDesc(sportsId).stream().map(ClubResponse::toClubResponse).toList();
    }

    // club 수정
    public ClubResponse update(Long id, ClubUpdateRequest request) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id에 해당하는 Club을 찾을 수 없습니다"));

        club.update(request.getClubName(), request.getClubContent(), request.getClubPlace(), request.getClubDate(), request.getClubTotalNumber(), request.getClubParticipateNumber(), request.getClubQnaLink(), request.getClubParticipateLink(), request.getClubCost(), request.getClubImage(), request.getClubStatus());

        return ClubResponse.toClubResponse(club);
    }

    // id에 해당하는 club 삭제
    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }

    public ClubResponse updateClubStatus(Long id) {
        Club club = clubRepository.findByClubId(id);
        if (club.getClubStatus().equals("모집 완료")) {
            throw new IllegalArgumentException("이미 모집 완료된 모임 입니다");
        }

        club.setClubStatus("모집 완료");
        return ClubResponse.toClubResponse(club);
    }

    // 필터링 검색
    public List<ClubResponse> findBySearchOptions(Long sportsId, ClubSearchRequest clubSearchRequest) {
        return clubRepositoryCustom.findBySearchOption(sportsId, clubSearchRequest.getClubStatus(), clubSearchRequest.getClubDate(), clubSearchRequest.getClubPlace(), clubSearchRequest.getClubMinCost(), clubSearchRequest.getClubMaxCost(), clubSearchRequest.getParticipantsCount(), clubSearchRequest.getSearchCondition(), clubSearchRequest.getSearchText())
                .stream().map(ClubResponse::toClubResponse).toList();
    }

    // 소모임 신청
    public ClubApplicationResponse saveApplication(Long clubId, ClubApplicationRequest clubApplicationRequest, User applicant) {
        Club club = clubRepository.findByClubId(clubId);
        if (club == null) {
            throw new IllegalArgumentException("Invalid clubId: " + clubId);
        }

        User leader = club.getUser();
        if (leader == null) {
            throw new IllegalArgumentException("No Leader");
        }

        ClubApplication clubApplication = new ClubApplication(clubApplicationRequest.getApplicantIntro(), club, applicant, leader);
        System.out.println("club: " + clubApplication.getClub().getClubName());
        System.out.println("leader: " + clubApplication.getLeader().getName());
        System.out.println("applicant: " + clubApplication.getApplicant().getName());
        System.out.println("intro: " + clubApplication.getApplicantIntro());

        clubApplicationRepository.save(clubApplication);
        return ClubApplicationResponse.toClubApplicationResponse(clubApplication);


    }

    public ClubApplication getApplicationByClubAndApplicant(Long id, User user) {
        Club club = clubRepository.findByClubId(id);
        return clubApplicationRepository.findByClubAndApplicant(club, user);
    }
}
