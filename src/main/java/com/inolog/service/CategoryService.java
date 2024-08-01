package com.inolog.service;

import com.inolog.domain.Category;
import com.inolog.exception.CategoryNotFound;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.category.CategoryEdit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Category> getList() {
        return categoryRepository.findAll();
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
