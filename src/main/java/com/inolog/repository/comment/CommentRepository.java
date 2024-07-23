package com.inolog.repository.comment;

import com.inolog.domain.Comment;
import com.inolog.request.comment.CommentSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
