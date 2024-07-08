package com.inolog;

import com.inolog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class InologApplication {

    public static void main(String[] args) {
        SpringApplication.run(InologApplication.class, args);
    }

}
