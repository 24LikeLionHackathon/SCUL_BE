package com.likelion.scul.common.service;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.repository.FollowRepository;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.stereotype.Service;

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
}
