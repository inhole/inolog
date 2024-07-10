package com.inolog.controller;

import com.inolog.config.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String main() {
        return "메인 페이지입니다.";
    }

    @GetMapping("/user") // security 에서 user 정보를 받기 위해 @AuthenticationPrincipal 추가
    public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return "사용자 페이지입니다. 😊";
    }

    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지입니다. 😎";
    }
}
