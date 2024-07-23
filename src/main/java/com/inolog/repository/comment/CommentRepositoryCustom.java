package com.inolog.repository.comment;

import com.inolog.domain.Comment;
import com.inolog.domain.Post;
import com.inolog.request.comment.CommentSearch;
import com.inolog.request.post.PostSearch;
import org.springframework.data.domain.Page;

public interface CommentRepositoryCustom {

    Page<Comment> getList(Long postId, CommentSearch commentSearch);
}
