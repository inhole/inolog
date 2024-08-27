package com.inolog.response;

import com.inolog.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String categoryName;
    private final LocalDateTime regDate;
    private final Long hits;

    /**
     *  생성자 오버로딩
     */
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryName = post.getCategory().getName();
        this.regDate = post.getRegDate();
        this.hits = post.getHits();
    }
}
