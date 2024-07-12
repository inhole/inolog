package com.inolog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = InologMockSecurityContext.class)
public @interface InologMockUser {

    String name() default "이인호";

    String email() default "sylee74133@gmail.com";

    String password() default "";

//    String role() default "ROLE_ADMIN";
}
