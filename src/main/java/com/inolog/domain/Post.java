package com.inolog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // key

    private String title;

    @Lob
    private String content; // 긴텍스트

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
