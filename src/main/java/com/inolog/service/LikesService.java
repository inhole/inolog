package com.inolog.service;

import com.inolog.domain.Likes;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.exception.post.LikesNotFound;
import com.inolog.exception.post.PostNotFound;
import com.inolog.exception.auth.UserNotFound;
import com.inolog.repository.likes.LikesRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.user.UserRepository;
import com.inolog.response.LikesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void insert(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        Likes likes = Likes.builder()
                .post(post)
                .user(user)
                .build();
        likesRepository.save(likes);
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        Likes likes = likesRepository.findByUserAndPost(user, post).orElseThrow(LikesNotFound::new);
        likesRepository.delete(likes);
    }

    public LikesResponse getCountLikes(Long postId, Long userId) {
        Long likesCount = likesRepository.countByPostId(postId);
        Long likesYn = likesRepository.countByUserIdAndPostId(userId, postId);

        LikesResponse likesResponse = LikesResponse.builder()
                .likesCount(likesCount)
                .likesYn(likesYn > 0 ? true : false)
                .build();
        return likesResponse;
    }
}
