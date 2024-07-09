package com.inolog.config;

import com.inolog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig  implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

}
