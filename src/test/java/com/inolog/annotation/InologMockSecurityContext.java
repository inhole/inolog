package com.inolog.annotation;

import com.inolog.config.UserPrincipal;
import com.inolog.domain.User;
import com.inolog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class InologMockSecurityContext implements WithSecurityContextFactory<InologMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(InologMockUser annotation) {
        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .role(annotation.role())
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);

//        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getValue()))
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
