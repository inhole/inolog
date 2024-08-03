package com.inolog.request.post;

import com.inolog.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    @NotBlank(message = "카테고리를 입력해주세요.")
    private Category category;

    @Builder
    public PostCreate(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
