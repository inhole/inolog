package com.inolog.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * 스프링 시큐리티가 인증된 유저를 담는 클래스이다. UserDetails를 상속 받는 User를 구현하여 커스텀하게 작성할 수 있다.
 */
public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(com.inolog.domain.User user) {
        super(user.getEmail(), user.getPassword(),
                List.of(
                        // ... TODO : DB에 저장된 회원 권한으로 적용
                        // 역할을 주려고 하면 앞에 'ROLE_'을 붙혀야 함
                        new SimpleGrantedAuthority("ROLE_ADMIN")

                ));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
