package com.inolog.service;

import com.inolog.crypto.PasswordEncoder;
import com.inolog.domain.User;
import com.inolog.exception.AlreadyExistsEmailException;
import com.inolog.exception.InvalidSigninInformation;
import com.inolog.repository.UserRepository;
import com.inolog.request.Login;
import com.inolog.request.Signup;
import com.inolog.crypto.ScryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     * @param login
     * @return
     */
    @Transactional
    public Long signin(Login login) {
        // email 조회
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        var matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
        // 암호화 된 password 검증
        if(!matches) {
            throw new InvalidSigninInformation();
        }

        // 안씀
//        Session session = user.addSession();

        return user.getId();
    }

    /**
     * 회원가입
     * @param signup
     */
    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var user = User.builder()
                .email(signup.getEmail())
                .password(encryptedPassword)
                .name(signup.getName())
                .build();
        userRepository.save(user);
    }
}
