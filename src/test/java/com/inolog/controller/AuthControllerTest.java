package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.domain.User;
import com.inolog.repository.PostRepository;
import com.inolog.repository.SessionRepository;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.request.PostCreate;
import com.inolog.service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {
        // given
        userRepository.save(User.builder()
                .name("ino")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build());

        // Scrypt, Bcrypt password 암호화

        Login login = Login.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .content(json)
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공후 세션 1개 생성")
    void test2() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("ino")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build());

        // Scrypt, Bcrypt password 암호화

        Login login = Login.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());


        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공후 세션 응답")
    void test3() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("ino")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build());

        // Scrypt, Bcrypt password 암호화

        Login login = Login.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }

}