package com.inolog.config;

import com.inolog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final PostRepository postRepository;

    /**
     * Spring Security 메서드 수준의 보안 핸들러 설정
     * @return
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handle = new DefaultMethodSecurityExpressionHandler();
        handle.setPermissionEvaluator(new InologPermissionEvaluator(postRepository));
        return handle;
    }
}
