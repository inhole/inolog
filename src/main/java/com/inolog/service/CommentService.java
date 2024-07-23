package com.inolog.service;

import com.inolog.domain.Comment;
import com.inolog.domain.Post;
import com.inolog.exception.CommentNotFound;
import com.inolog.exception.InvalidPassword;
import com.inolog.exception.PostNotFound;
import com.inolog.repository.comment.CommentRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.request.comment.CommentCreate;
import com.inolog.request.comment.CommentDelete;
import com.inolog.request.comment.CommentSearch;
import com.inolog.response.CommentResponse;
import com.inolog.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        // 조회한 Post 엔티티는 영속성 컨텍스트에 의해 관리됩니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        // CascadeType.ALL로 설정된 Comment 엔티티가 자동으로 영속성 컨텍스트에 추가됩니다.
        // 트랜잭션이 커밋될 때, 영속성 컨텍스트에 있는 모든 변경 사항이 데이터베이스에 자동으로 반영됩니다.
        post.addComment(comment);
    }

    public PagingResponse<CommentResponse> getList(Long postId, CommentSearch commentSearch) {
        Page<Comment> commentPage = commentRepository.getList(postId, commentSearch);
        PagingResponse<CommentResponse> commentList = new PagingResponse<>(commentPage, CommentResponse.class);

        return commentList;
    }

    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if (!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
