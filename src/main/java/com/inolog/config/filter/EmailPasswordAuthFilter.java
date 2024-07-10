package com.inolog.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

/**
 * EmailPasswordAuthFilter는 사용자가 이메일과 비밀번호를 사용하여 인증할 때
 * Spring Security의 인증 과정을 처리하는 필터입니다.
 */
public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    /**
     * EmailPasswordAuthFilter 생성자.
     * "/auth/login" URL로 들어오는 요청을 처리하기 위해 초기화합니다.
     *
     * @param objectMapper 요청 본문을 파싱하기 위한 ObjectMapper
     */
    public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper) {
        super(loginUrl);
        this.objectMapper = objectMapper;
    }

    /**
     * 인증을 시도하는 메소드.
     * HTTP 요청으로부터 이메일과 비밀번호를 추출하고, 이를 이용하여 인증을 시도합니다.
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @return Authentication 객체, 인증 성공 시 인증된 객체를 반환합니다.
     * @throws AuthenticationException 인증 실패 시 예외를 던집니다.
     * @throws IOException             요청 본문을 읽는 중 IO 예외가 발생할 수 있습니다.
     * @throws ServletException        서블릿 관련 예외가 발생할 수 있습니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 1. 요청 본문에서 이메일과 비밀번호를 읽어 EmailPassword 객체로 변환합니다.
        EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

        // 2. 이메일과 비밀번호를 사용하여 인증 토큰을 생성합니다.
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPassword.email,
                emailPassword.password
        );

        // 3. 요청의 추가 정보를 토큰의 details에 설정합니다.
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));

        // 4. AuthenticationManager를 사용하여 인증을 시도합니다.
        return this.getAuthenticationManager().authenticate(token);
    }

    @Getter
    private static class EmailPassword {
        private String email;
        private String password;
    }
}
