package com.inolog.controller;

import com.inolog.domain.Category;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.category.CategoryEdit;
import com.inolog.response.CategoryResponse;
import com.inolog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 카테고리 조회
     * @return
     */
    @GetMapping("/category")
    public List<CategoryResponse> getList() {
        return categoryService.getList();
    }

    /**
     * 카테고리 수정
     * @param categoryId
     * @param request
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/category/{categoryId}")
    public void edit(@PathVariable Long categoryId, @RequestBody @Valid CategoryEdit request) {
        categoryService.edit(categoryId, request);
    }

    /**
     * 카테고리 삭제
     * @param categoryId
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/category/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }
}
