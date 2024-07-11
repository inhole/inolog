package com.inolog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserFactory.class)
public @interface CustomWithMockUser {

    // 어노테이션 생성

    String username() default "sylee74133@gmail.com";

    String password() default "1234";

    int level() default 5;

    String mobileNumber = "01000000000";
}
