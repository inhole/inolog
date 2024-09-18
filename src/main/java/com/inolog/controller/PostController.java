package com.inolog.controller;

import com.inolog.config.UserPrincipal;
import com.inolog.request.post.PostCreate;
import com.inolog.request.post.PostEdit;
import com.inolog.request.post.PostSearch;
import com.inolog.response.PagingResponse;
import com.inolog.response.PostResponse;
import com.inolog.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

     /**
     * 게시글 등록
     * @param request
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostCreate request) {
        postService.write(userPrincipal.getUserId(), request);
    }

    /**
     * 게시글 한건 조회
     * @param postId
     * @return
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     * 게시글 여러개 조회
     * @param postSearch
     * @return
     */
    @GetMapping("/posts")
    public PagingResponse<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    /**
     * 게시글 수정
     * @param postId
     * @param request
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    /**
     * 게시글 삭제
     * @param postId
     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

    /**
     * 게시글 조회수
     * @param postId
     */
    @PatchMapping("/posts/{postId}/hits")
    public void upHits(@PathVariable Long postId,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        // 1. 중복 방지를 위한 쿠키를 가져온다
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElseGet(() -> new Cookie[0]);

        // 2. 쿠키가 있으면 cookie에 넣고 아니면 조회수 증가 및 쿠키 생성
        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("postHits"))
                .findFirst()
                .orElseGet(() -> {
                    postService.upHits(postId);
                    return new Cookie("postHits", "[" + postId + "]");
                });

        // 3. postId 에 맞는 쿠키가 없으면 조회수 증가 및 쿠키 생성
        if (!cookie.getValue().contains("[" + postId + "]")) {
            postService.upHits(postId);
            cookie.setValue(cookie.getValue() + "[" + postId + "]");
        }

        // 4. 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
