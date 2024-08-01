package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Category;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.category.CategoryEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CategoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;


    @BeforeEach
    public void clean() {
        categoryRepository.deleteAll();
    }


    @Test
    @InologMockUser
    @DisplayName("카테고리 생성")
    void test1() throws Exception {
        // given
        CategoryCreate categoryCreate = new CategoryCreate("SpringBoot");
        String json = objectMapper.writeValueAsString(categoryCreate);

        // when
        mockMvc.perform(post("/category")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Category category = categoryRepository.findAll().get(0);
        assertEquals(1L, categoryRepository.count());
        assertEquals(category.getName(), "SpringBoot");
    }

    @Test
    @InologMockUser
    @DisplayName("카테고리 조회")
    void test2() throws Exception {
        // given
        CategoryCreate categoryCreate = new CategoryCreate("SpringBoot");
        String json = objectMapper.writeValueAsString(categoryCreate);

        // when
        mockMvc.perform(post("/category")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Category category = categoryRepository.findAll().get(0);
        assertEquals(1L, categoryRepository.count());
        assertEquals(category.getName(), "SpringBoot");
    }

    @Test
    @InologMockUser
    @DisplayName("카테고리 수정")
    void test3() throws Exception {
        // given
        Category requestCategory = Category.builder()
                .name("Java15")
                .build();
        categoryRepository.save(requestCategory);

        CategoryEdit categoryEdit = new CategoryEdit("Java17");
        String json = objectMapper.writeValueAsString(categoryEdit);

        // when
        mockMvc.perform(patch("/category/{categoryId}", requestCategory.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Category category = categoryRepository.findAll().get(0);
        assertEquals(1L, categoryRepository.count());
        assertEquals(category.getName(), "Java17");
    }

    @Test
    @InologMockUser
    @DisplayName("카테고리 삭제")
    void test4() throws Exception {
        // given
        Category requestCategory = Category.builder()
                .name("Java15")
                .build();
        categoryRepository.save(requestCategory);

        // when
        mockMvc.perform(delete("/category/{categoryId}", requestCategory.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(0, categoryRepository.count());
    }
}