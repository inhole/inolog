package com.inolog.controller;

import com.inolog.config.UserPrincipal;
import com.inolog.response.LikesResponse;
import com.inolog.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts/likes/{postId}")
public class LikesController {

    private final LikesService likesService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    public void insert(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        likesService.insert(postId, userPrincipal.getUserId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        likesService.delete(postId, userPrincipal.getUserId());
    }

    @GetMapping
    public ResponseEntity<LikesResponse> get(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = 0L;

        if (authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            userId = principal.getUserId();
        }

        LikesResponse likesResponse = likesService.getCountLikes(postId, userId);

        return ResponseEntity.ok().body(likesResponse);
    }
}
