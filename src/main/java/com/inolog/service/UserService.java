package com.inolog.service;

import com.inolog.domain.User;
import com.inolog.exception.auth.UserNotFound;
import com.inolog.repository.user.UserRepository;
import com.inolog.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        return new UserResponse(user);
    }
}
