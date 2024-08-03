package com.inolog.repository.category;

import com.inolog.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.inolog.domain.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Category> getList() {
        return jpaQueryFactory.selectFrom(category)
                .orderBy(category.id.desc())
                .fetch();
    }
}
