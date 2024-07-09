package com.inolog.config;

import com.inolog.config.data.UserSession;
import com.inolog.domain.Session;
import com.inolog.exception.Unauthorized;
import com.inolog.repository.SessionRepository;
import com.inolog.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

/*
    # Argument Resolver는 Interceptor 가 처리된 후 처리된다

    ## 동작 순서
    1. Client 가 요청한다(Request).

    2. Dispatcher Servlet에서 Request를 처리한다.

    3. 요청을 분석하여 Request에 대한 Hadler Mapping을 한다.
    - RequestMappingHandlerAdapter(핸들러 매핑에 맞는 어댑터 결정).
    - Interceptor 처리
    - Argument Resolver 처리 (등록한 리졸버에 대응되는 파라미터 바인딩)
    - Message Converter 처리

    4. Controller Method invoke
*/
    private final JwtUtil jwtUtil;

    /**
     * parameter가 해당 resolver를 지원하는 여부 확인
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    /**
     * Method parameter를 argument value로 변환, 바인딩 하는 역할
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jws = webRequest.getHeader("Authorization");

        long userId = jwtUtil.getUserId(jws);

        return new UserSession(userId);
    }
}
