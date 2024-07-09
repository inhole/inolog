package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.domain.Session;
import com.inolog.domain.User;
import com.inolog.repository.PostRepository;
import com.inolog.repository.SessionRepository;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.request.PostCreate;
import com.inolog.service.PostService;
import com.inolog.util.JwtUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 후 토큰 확인")
    void test1() throws Exception {
        // given
        userRepository.save(User.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("ino")
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


    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("ino")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        String jws = jwtUtil.generateToken(user.getId());

        // when
        mockMvc.perform(get("/foo")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .header("Authorization", jws)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


}