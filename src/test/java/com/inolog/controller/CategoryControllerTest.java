package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Category;
import com.inolog.repository.category.CategoryRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.user.UserRepository;
import com.inolog.request.category.CategoryCreate;
import com.inolog.request.post.PostCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
}