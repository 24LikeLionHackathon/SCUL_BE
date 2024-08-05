package com.likelion.scul.common.service;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.follow.FollowNumResponse;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.dto.follow.FollowResponse;
import com.likelion.scul.common.repository.FollowRepository;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

//    public Follow saveFollow(String email,Long postId) {
//        Follow follow = makeFollow(email,postId);
//        return followRepository.save(follow);
//    }
//
//    private Follow makeFollow(FollowRequest request) {
//        Follow newFollow = new Follow();
//        User follower = userRepository.findById(request.getFollowerUserId()).get();
//        User followed = userRepository.findById(request.getFollowedUserId()).get();
//        newFollow.setFollower(follower);
//        newFollow.setFollowed(followed);
//        return newFollow;
//    }

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
}

