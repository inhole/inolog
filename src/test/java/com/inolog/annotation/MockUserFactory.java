package com.inolog.annotation;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        // ... 임의의 유저 정보를 저장
        // String level = annotation.username();
        return null;
    }
}
