package com.inolog.config;

import com.inolog.domain.User;
import com.inolog.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

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
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                        .anyRequest().authenticated()
                )
                // login 설정
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                )
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
