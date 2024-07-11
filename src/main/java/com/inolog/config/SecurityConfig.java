package com.inolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.config.filter.EmailPasswordAuthFilter;
import com.inolog.config.handler.Http401Handler;
import com.inolog.config.handler.Http403Handler;
import com.inolog.config.handler.LoginFailHandler;
import com.inolog.config.handler.LoginSuccessHandler;
import com.inolog.domain.User;
import com.inolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity // Controller 단에서 권한 생성을 위해 추가
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

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
                        .anyRequest().permitAll()
                )
                // form 에서 json 으로 요청 받기 위해 설정
                .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                // login 설정
//                .formLogin(login -> login
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/auth/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/")
//                        .failureHandler(new LoginFailHandler(objectMapper)) // login 실패 handler
//                )
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
     * 커스텀마이징한 유저 정보 인증 필터
     *
     * @return
     */
    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        // 로그인 성공 url
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
        // 로그인 실패
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        // 세션 발급
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        // 세션 유효기간 1달 설정
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        filter.setRememberMeServices(rememberMeServices);

        return filter;
    }

    /**
     * provider 설정
     *
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    /**
     * 요청 받은 유저정보로 메소드에서 확인
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {

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
