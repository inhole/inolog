package com.inolog.controller;

import com.inolog.request.category.CategoryCreate;
import com.inolog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 작성
     * @param categoryCreate
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/category")
    public void write(@RequestBody @Valid CategoryCreate categoryCreate) {
        categoryService.write(categoryCreate);
    }
}
