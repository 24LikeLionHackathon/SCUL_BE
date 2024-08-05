package com.likelion.scul.club;

import com.likelion.scul.board.service.S3Service;
import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.ClubApplication;
import com.likelion.scul.club.domain.ClubUser;
import com.likelion.scul.club.dto.*;
import com.likelion.scul.club.repository.ClubApplicationRepository;
import com.likelion.scul.club.repository.ClubRepository;
import com.likelion.scul.club.repository.ClubRepositoryCustom;
import com.likelion.scul.club.repository.ClubUserRepository;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.repository.SportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final SportsRepository sportsRepository;
    private final ClubRepositoryCustom clubRepositoryCustom;
    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubUserRepository clubUserRepository;
    private final S3Service s3Service;

    @Autowired
    public ClubService(ClubRepository clubRepository, SportsRepository sportsRepository, ClubRepositoryCustom clubRepositoryCustom, ClubApplicationRepository clubApplicationRepository, ClubUserRepository clubUserRepository, S3Service s3Service) {
        this.clubRepository = clubRepository;
        this.sportsRepository = sportsRepository;
        this.clubRepositoryCustom = clubRepositoryCustom;
        this.clubApplicationRepository = clubApplicationRepository;
        this.clubUserRepository = clubUserRepository;
        this.s3Service = s3Service;
    }

    // club 생성
    public ClubResponse save(ClubRequest clubRequest, User user) throws IOException {
        String status = "모집 중";
        int participateNumber = 1;

        Sports sports = sportsRepository.findBySportsName(clubRequest.getSportsName()).orElseThrow(()-> new IllegalArgumentException("해당하는 sports name이 없습니다"));

        String imageUrl;
        if(clubRequest.getClubImage() != null) {
            String key = s3Service.uploadFile(clubRequest.getClubImage());
            imageUrl = s3Service.getFileUrl(key).toString();
        }
        else{
            imageUrl = sports.getSportsDefaultImg();
        }

        Club club = new Club(clubRequest, status, user, sports, imageUrl, participateNumber);

        System.out.println("club name: " + club.getClubName());
        System.out.println("club participate number: " + club.getClubParticipateNumber());

        clubRepository.save(club);

        ClubUser clubUser = new ClubUser(club, user);
        clubUserRepository.save(clubUser);

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
    public ClubResponse update(Long id, ClubUpdateRequest request) throws IOException {
        Club club = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id에 해당하는 Club을 찾을 수 없습니다"));

        String key = s3Service.uploadFile(request.getClubImage());
        String imageUrl = s3Service.getFileUrl(key).toString();

        club.update(request.getClubName(), request.getClubContent(), request.getClubPlace(), request.getClubDate(), request.getClubTotalNumber(), request.getClubParticipateNumber(), request.getClubQnaLink(), request.getClubParticipateLink(), request.getClubCost(), imageUrl, request.getClubStatus());
        return ClubResponse.toClubResponse(club);
    }

    // id에 해당하는 club 삭제
    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }

    // 모집 중 -> 마감
    public ClubResponse updateClubStatus(Long id) {
        Club club = clubRepository.findByClubId(id);
        if (club.getClubStatus().equals("마감")) {
            throw new IllegalArgumentException("이미 마감된 모임 입니다");
        }

        club.setClubStatus("마감");
        clubRepository.save(club);
        return ClubResponse.toClubResponse(club);
    }

    // 필터링 검색
    public List<ClubResponse> findBySearchOptions(Long sportsId, ClubSearchRequest clubSearchRequest) {
        return clubRepositoryCustom.findBySearchOption(sportsId, clubSearchRequest.getClubStatus(), clubSearchRequest.getClubDate(), clubSearchRequest.getClubPlace(), clubSearchRequest.getClubMinCost(), clubSearchRequest.getClubMaxCost(), clubSearchRequest.getTotalMinCount(), clubSearchRequest.getTotalMaxCount(), clubSearchRequest.getSearchCondition(), clubSearchRequest.getSearchText())
                .stream().map(ClubResponse::toClubResponse).toList();
    }

    // 소모임 신청
    public ClubApplicationResponse saveApplication(Long clubId, ClubApplicationRequest clubApplicationRequest, User applicant) {
        Boolean isApprove = false;
        Club club = clubRepository.findByClubId(clubId);
        if (club == null) {
            throw new IllegalArgumentException("Invalid clubId: " + clubId);
        }

        User leader = club.getUser();
        if (leader == null) {
            throw new IllegalArgumentException("No Leader");
        }

        ClubApplication clubApplication = new ClubApplication(clubApplicationRequest.getApplicantIntro(), club, applicant, leader, isApprove);

        clubApplicationRepository.save(clubApplication);
        return ClubApplicationResponse.toClubApplicationResponse(clubApplication);


    }

    public ClubApplication getApplicationByClubAndApplicant(Long id, User user) {
        Club club = clubRepository.findByClubId(id);
        return clubApplicationRepository.findByClubAndApplicant(club, user);
    }

    // 소모임 신청 승인
    public ClubApplicationResponse approveApplication(Long clubApplicationId, ClubApplicationApproveRequest clubApplicationApproveRequest, User user) {
        ClubApplication clubApplication = clubApplicationRepository.findByClubApplicationId(clubApplicationId);

        if (!Objects.equals(clubApplication.getLeader().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("승인이 허가된 사용자가 아닙니다");
        }

        if (clubApplicationApproveRequest.getApprove()) {
            Club club = clubRepository.findByClubId(clubApplication.getClub().getClubId());
            int originCount = club.getClubParticipateNumber();
            club.setClubParticipateNumber(originCount + 1);
            clubRepository.save(club);

            clubApplication.setIsApprove(true);
            clubApplicationRepository.save(clubApplication);

            ClubUser clubUser = new ClubUser(club, clubApplication.getApplicant());
            clubUserRepository.save(clubUser);
        }
        return ClubApplicationResponse.toClubApplicationResponse(clubApplication);
    }

    public List<Long> getClubIdsForUser(User user) {
        return clubUserRepository.findClubIdsByUser(user);
    }

    public List<ClubResponse> findMyClub(User user) {
        List<Long> clubIds = getClubIdsForUser(user);
        return clubRepository.findByClubIdIn(clubIds).stream().map(ClubResponse::toClubResponse).toList();
    }
}
