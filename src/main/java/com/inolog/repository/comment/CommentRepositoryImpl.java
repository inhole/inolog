package com.inolog.repository.comment;

import com.inolog.domain.Comment;
import com.inolog.domain.Post;
import com.inolog.request.comment.CommentSearch;
import com.inolog.request.post.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.inolog.domain.QComment.comment;
import static com.inolog.domain.QPost.post;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> getList(Long postId, CommentSearch commentSearch) {
        Long totalCount = jpaQueryFactory.select(comment.count())
                .from(comment)
                .where(post.id.eq(postId))
                .fetchFirst();

        List<Comment> items = jpaQueryFactory.selectFrom(comment)
                .limit(commentSearch.getSize())
                .offset(commentSearch.getOffset())
                .orderBy(comment.id.desc())
                .fetch();

        return new PageImpl<>(items, commentSearch.getPageable(), totalCount);
    }
}
