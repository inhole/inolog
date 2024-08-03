package com.inolog.service;

import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Category;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.category.CategoryEdit;
import com.inolog.response.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Category category = categoryRepository.getList().get(0);
        assertEquals("SpringBoot", category.getName());
    }

    @Test
    @DisplayName("카테고리 조회")
    @InologMockUser
    void test2() {
        // given
        List<Category> requestCategories = IntStream.range(1, 6)
                .mapToObj(i -> Category.builder()
                        .name("Java" + i)
                        .build())
                .collect(Collectors.toList());
        categoryRepository.saveAll(requestCategories);

        // when
        List<CategoryResponse> categoryList = categoryService.getList();

        // then
        assertEquals(5L, categoryRepository.count());
        assertEquals("Java1", categoryList.get(0).getName());
    }

    @Test
    @DisplayName("카테고리 수정")
    @InologMockUser
    void test3() {
        // given
        Category requestCategory = Category.builder()
                .name("Java16")
                .build();
        categoryRepository.save(requestCategory);

        CategoryEdit categoryEdit = new CategoryEdit("Java17");

        // when
        categoryService.edit(requestCategory.getId(), categoryEdit);

        // then
        Category category = categoryRepository.getList().get(0);
        assertEquals("Java17", category.getName());
    }

    @Test
    @DisplayName("카테고리 삭제")
    @InologMockUser
    void test4() {
        // given
        Category requestCategory = Category.builder()
                .name("Java16")
                .build();
        categoryRepository.save(requestCategory);

        // when
        categoryService.delete(requestCategory.getId());

        // then
        assertEquals(0, categoryRepository.count());
    }
}