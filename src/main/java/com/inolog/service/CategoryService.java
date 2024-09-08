package com.inolog.service;

import com.inolog.domain.Category;
import com.inolog.exception.category.CategoryNotFound;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.category.CategoryEdit;
import com.inolog.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void write(CategoryCreate categoryCreate) {
        Category category = Category.builder()
                .name(categoryCreate.getName())
                .build();
        categoryRepository.save(category);
    }

    public List<CategoryResponse> getList() {
        List<Category> categoryList = categoryRepository.getList();
        return categoryList.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long categoryId, CategoryEdit request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFound::new);

        category.edit(request);
    }

    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
