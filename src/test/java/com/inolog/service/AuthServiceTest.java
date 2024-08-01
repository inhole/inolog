package com.inolog.service;

import com.inolog.domain.User;
import com.inolog.exception.AlreadyExistsEmailException;
import com.inolog.repository.user.UserRepository;
import com.inolog.request.user.Signup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("이인호")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("sylee74133@gmail.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertEquals("이인호", user.getName());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
        User user = User.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("이인호")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .name("이인호")
                .build();

        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup) );
    }


}