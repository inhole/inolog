package com.inolog.repository.post;

import com.inolog.domain.Post;
import com.inolog.request.post.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.inolog.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> getList(PostSearch postSearch) {

        Long totalCount;
        List<Post> items;

        // 기본 쿼리 설정
        var query = jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc());

        // 카테고리 필터링 조건 추가
        if (postSearch.getCategoryId() != 0L && postSearch.getCategoryId() != null) {
            query = query.where(post.category.id.eq(postSearch.getCategoryId()));
        }

        // 총 개수 조회 쿼리
        totalCount = jpaQueryFactory.select(post.count())
                .from(post)
                .where(query.getMetadata().getWhere()) // 동일한 where 조건 사용
                .fetchFirst();

        // 최종 아이템 조회
        items = query.fetch();

        return new PageImpl<>(items, postSearch.getPageable(), totalCount);
    }
}
