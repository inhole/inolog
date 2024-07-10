package com.inolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.config.handler.Http401Handler;
import com.inolog.config.handler.Http403Handler;
import com.inolog.config.handler.LoginFailHandler;
import com.inolog.domain.User;
import com.inolog.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    /**
     * 스프링 시큐리티 기능 비활성화
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 해당 경로는 접근 가능
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
                .requestMatchers(toH2Console());
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 권한이 없으면 해당 uri 제외하고 접근 불가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 관리자 역할이 ( hasRole('ADMIN') ) 있고 쓰기 권한도 ( hasAuthority('WRITE') ) 있어야 접근 가능
//                            .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')"))
                        .anyRequest().authenticated()
                )
                // login 설정
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureHandler(new LoginFailHandler(objectMapper)) // login 실패 handler
                )
                // 예외 처리 custom
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper)); // 인가 실패 ( 권한이 없는 경우 )
                    e.authenticationEntryPoint(new Http401Handler(objectMapper)); // 인증 실패 ( 로그인 안 한 경우 )
                })
                // cookie 설정
                .rememberMe(rm -> rm
                        .rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000) // 만료일 한달
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * 요청 받은 유저정보로 메소드에서 확인
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
            return new UserPrincipal(user);
        };

        // memory 상에서 임시로 만듬
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        UserDetails user = User
//                .withUsername("ino")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//        manager.createUser(user);
//        return manager;
    }

    /**
     * User Password 인코딩 (Scrypt)
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64);

        // test 용
//        return NoOpPasswordEncoder.getInstance();
    }
}
