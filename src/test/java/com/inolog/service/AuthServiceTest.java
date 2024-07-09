package com.inolog.service;

import com.inolog.domain.User;
import com.inolog.exception.AlreadyExistsEmailException;
import com.inolog.exception.InvalidSigninInformation;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.request.Signup;
import com.inolog.crypto.ScryptPasswordEncoder;
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
        assertEquals("1234", user.getPassword());
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

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("sylee74133@gmail.com")
                .password(encryptedPassword)
                .name("이인호")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(login);

        // then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("sylee74133@gmail.com")
                .password(encryptedPassword)
                .name("이인호")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("sylee74133@gmail.com")
                .password("5678")
                .build();

        // expected
        assertThrows(InvalidSigninInformation.class,
                () -> authService.signin(login));

    }

}