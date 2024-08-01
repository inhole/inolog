package com.inolog.service;

import com.inolog.domain.Category;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
