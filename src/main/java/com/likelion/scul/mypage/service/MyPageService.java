package com.likelion.scul.mypage.service;

import com.likelion.scul.board.domain.Comment;
import com.likelion.scul.board.domain.Like;
import com.likelion.scul.board.domain.Post;
import com.likelion.scul.board.repository.CommentRepository;
import com.likelion.scul.board.repository.LikeRepository;
import com.likelion.scul.board.repository.PostRepository;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.domain.UserSports;
import com.likelion.scul.common.repository.UserRepository;
import com.likelion.scul.common.repository.UserSportsRepository;
import com.likelion.scul.common.repository.FollowRepository;
import com.likelion.scul.club.ClubRepository;
import com.likelion.scul.mypage.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserSportsRepository userSportsRepository;
    private final FollowRepository followRepository;
    private final ClubRepository clubRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public MyPageService(UserRepository userRepository,
                         UserSportsRepository userSportsRepository,
                         FollowRepository followRepository,
                         ClubRepository clubRepository,
                         PostRepository postRepository,
                         CommentRepository commentRepository,
                         LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.userSportsRepository = userSportsRepository;
        this.followRepository = followRepository;
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }
    public MyPageHeaderDto getHeaderInfo(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // Find the user's sports with priority 0
        List<UserSports> userSportsList = userSportsRepository.findByUserUserId(user.getUserId());
        String topPrioritySportsName = userSportsList.stream()
                .filter(us -> us.getSportsPriority() == 0)
                .map(us -> us.getSports().getSportsName())
                .findFirst()
                .orElse(null);

        int followerNum = followRepository.countByFollowed(user);
        int followedNum = followRepository.countByFollower(user);
        int participatingClubNum = clubRepository.countByUser(user);
        // Get user image URL
        String userProfileImageUrl = user.getUserImage() != null ? user.getUserImage().getImageUrl() : null;

        return new MyPageHeaderDto(
                user.getNickname(),
                topPrioritySportsName,
                followerNum,
                followedNum,
                participatingClubNum,
                userProfileImageUrl
        );
    }

    public ActivityPostsDto getActivityPostsInfo(String email, int page) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // 사용자별 게시물을 페이지 단위로 조회
        Page<Post> postsPage = postRepository.findByUser(user, PageRequest.of(page - 1, 10));

        int totalPosts = (int) postsPage.getTotalElements();
        int totalComments = commentRepository.countByUser(user);
        int totalLikes = likeRepository.countByUser(user);
        int participatingClubs = clubRepository.countByUser(user);

        List<PostInfoDto> postsList = postsPage.getContent().stream().map(post -> {
            String postImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            int commentCount = post.getComments().size();

            return new PostInfoDto(
                    postImage,
                    post.getBoard().getSports().getSportsName(),
                    post.getBoard().getBoardName(),
                    post.getPostTitle(),
                    post.getCreatedAt(),
                    commentCount
            );
        }).collect(Collectors.toList());

        return new ActivityPostsDto(
                postsList,
                totalPosts,
                totalComments,
                totalLikes,
                participatingClubs
        );
    }

    public ActivityCommentsDto getActivityComments(String email, int page) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // 사용자별 댓글을 페이지 단위로 조회
        Page<Comment> commentsPage = commentRepository.findByUser(user, PageRequest.of(page - 1, 10));

        int totalPosts = postRepository.countByUser(user);
        int totalComments = (int) commentsPage.getTotalElements();
        int totalLikes = likeRepository.countByUser(user);
        int participatingClubs = clubRepository.countByUser(user);

        List<CommentInfoDto> commentList = commentsPage.getContent().stream().map(comment -> {
            return new CommentInfoDto(
                    comment.getCommentContent(),
                    comment.getCreatedAt(),
                    comment.getPost().getBoard().getSports().getSportsName(),
                    comment.getPost().getBoard().getBoardName(),
                    comment.getPost().getPostTitle()
            );
        }).collect(Collectors.toList());

        return new ActivityCommentsDto(
                commentList,
                totalPosts,
                totalComments,
                totalLikes,
                participatingClubs
        );
    }

    public LikedPostsDto getActivityLikes(String email, int page) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // 사용자가 좋아요를 누른 게시물 목록을 페이지 단위로 조회
        Page<Like> likesPage = likeRepository.findByUser(user, PageRequest.of(page - 1, 10));

        int totalPosts = postRepository.countByUser(user);
        int totalComments = commentRepository.countByUser(user);
        int totalLikes = (int) likesPage.getTotalElements();
        int participatingClubs = clubRepository.countByUser(user);

        List<LikedPostInfoDto> postsList = likesPage.getContent().stream().map(like -> {
            Post post = like.getPost();
            String postImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            int commentCount = post.getComments().size();

            return new LikedPostInfoDto(
                    postImage,
                    post.getBoard().getSports().getSportsName(),
                    post.getBoard().getBoardName(),
                    post.getPostTitle(),
                    post.getUser().getNickname(),
                    post.getCreatedAt(),
                    commentCount
            );
        }).collect(Collectors.toList());

        return new LikedPostsDto(
                postsList,
                totalPosts,
                totalComments,
                totalLikes,
                participatingClubs
        );
    }
}

