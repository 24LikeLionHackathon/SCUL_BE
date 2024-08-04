package com.likelion.scul.common.service;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.follow.FollowNumResponse;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.dto.follow.FollowResponse;
import com.likelion.scul.common.dto.follow.FollowProfileResponse;
import com.likelion.scul.common.repository.FollowRepository;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;
    private UserSportsService userSportsService;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository,
            UserSportsService userSportsService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userSportsService = userSportsService;
    }

    public Follow saveFollow(FollowRequest request) {
        Follow follow = makeFollow(request);
        return followRepository.save(follow);
    }

    private Follow makeFollow(FollowRequest request) {
        Follow newFollow = new Follow();
        User follower = userRepository.findById(request.getFollowerUserId()).get();
        User followed = userRepository.findById(request.getFollowedUserId()).get();
        newFollow.setFollower(follower);
        newFollow.setFollowed(followed);
        return newFollow;
    }

    public FollowResponse getFollows(Long userId) {
        FollowResponse response = new FollowResponse();
        response.setFollowers(getMyFollowers(userId));
        response.setFollowings(getMyFollowings(userId));
        return response;
    }

    private List<User> getMyFollowers(Long userId) {
        List<Follow> followerList = followRepository.findByfollowedUserId(userId);
        return followerList.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    private List<User> getMyFollowings(Long userId) {
        List<Follow> followingList = followRepository.findByfollowerUserId(userId);
        return followingList.stream()
                .map(Follow::getFollowed)
                .collect(Collectors.toList());
    }

    public void deleteFollow(Long followId) {
        followRepository.deleteById(followId);
    }

    public FollowNumResponse getFollowNum(Long userId) {
        FollowNumResponse response = new FollowNumResponse();
        response.setFollowerNumber(followRepository.countFollowersByUserId(userId));
        response.setFollowingNumber(followRepository.countFollowingByUserId(userId));

        return response;
    }

    public List<FollowProfileResponse> getFollowers(String email, int pageNum) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        List<Follow> follwerList = followRepository.findByfollowedUserId(user.getUserId());

        List<FollowProfileResponse> response = new ArrayList<>();
        for (Follow follow : follwerList) {
            User follower = follow.getFollower();
            FollowProfileResponse followProfileResponse = new FollowProfileResponse();
            followProfileResponse.setUserId(follower.getUserId());
            followProfileResponse.setNickName(follower.getNickname());
            followProfileResponse.setUserImageUrl(followProfileResponse.getUserImageUrl());
            followProfileResponse.setFavoriteSports(userSportsService.getFavoriteSportsNameByUser(follower));
            followProfileResponse.setFollowerNumber(followRepository.countFollowersByUserId(follower.getUserId()));
            followProfileResponse.setFollowingNumber(followRepository.countFollowingByUserId(follower.getUserId()));
            response.add(followProfileResponse);
        }

        if ( response.size() < 10 ) {
            return response;
        }
        int from = pageNum * 10 - 10 ;
        int to ;
        if ( (int) (response.size() / 10) < pageNum ){
            to = response.size() ;
        }
        else {
            to = pageNum * 10 ;
        }
        return response.subList(from, to);
    }

    public List<FollowProfileResponse> getFollowings(String email, int pageNum) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        List<Follow> follwerList = followRepository.findByfollowerUserId(user.getUserId());

        List<FollowProfileResponse> response = new ArrayList<>();
        for (Follow follow : follwerList) {
            User follower = follow.getFollowed();
            FollowProfileResponse followProfileResponse = new FollowProfileResponse();
            followProfileResponse.setUserId(follower.getUserId());
            followProfileResponse.setNickName(follower.getNickname());
            followProfileResponse.setUserImageUrl(followProfileResponse.getUserImageUrl());
            followProfileResponse.setFavoriteSports(userSportsService.getFavoriteSportsNameByUser(follower));
            followProfileResponse.setFollowingNumber(followRepository.countFollowersByUserId(follower.getUserId()));
            followProfileResponse.setFollowerNumber(followRepository.countFollowingByUserId(follower.getUserId()));
            response.add(followProfileResponse);
        }

        if ( response.size() < 10 ) {
            return response;
        }
        int from = pageNum * 10 - 10 ;
        int to ;
        if ( (int) (response.size() / 10) < pageNum ){
            to = response.size() ;
        }
        else {
            to = pageNum * 10 ;
        }
        return response.subList(from, to);
    }
}

