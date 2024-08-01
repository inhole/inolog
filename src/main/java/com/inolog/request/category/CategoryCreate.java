package com.inolog.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreate {

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;

    public CategoryCreate(String name) {
        this.name = name;
    }
}
