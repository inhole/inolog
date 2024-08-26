package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.domain.User;
import com.inolog.repository.user.UserRepository;
import com.inolog.request.user.Signup;
import com.inolog.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test3() throws Exception {
        // given
        Signup signup = Signup.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("이인호")
                .build();

        // when
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then

        Optional<User> member = userRepository.findByEmail(signup.getEmail());
        Assertions.assertEquals(member.get().getEmail(), signup.getEmail());
        Assertions.assertEquals(member.get().getPassword(), passwordEncoder.encode(signup.getPassword()));
        Assertions.assertEquals(member.get().getName(), signup.getName());
    }
}