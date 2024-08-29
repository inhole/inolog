package com.inolog.repository.likes;

import com.inolog.domain.Likes;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Long countByPostId(Long postId);

    Optional<Likes> findByUserAndPost(User user, Post post);

    Long countByUserIdAndPostId(Long userId, Long postId);
}
