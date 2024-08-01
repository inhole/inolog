package com.inolog.service;

import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Category;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void clean() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성")
    @InologMockUser
    void test1() {
        // given
        CategoryCreate categoryCreate = new CategoryCreate("SpringBoot");

        // when
        categoryService.write(categoryCreate);

        // then
        assertEquals(1L, categoryRepository.count());
        Category category = categoryRepository.findAll().get(0);
        assertEquals("SpringBoot", category.getName());
    }
}