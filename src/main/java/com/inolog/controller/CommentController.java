package com.inolog.controller;

import com.inolog.request.comment.CommentCreate;
import com.inolog.request.comment.CommentDelete;
import com.inolog.request.comment.CommentSearch;
import com.inolog.response.CommentResponse;
import com.inolog.response.PagingResponse;
import com.inolog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     * @param postId
     * @param request
     */
    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate request) {
        commentService.write(postId, request);
    }

    /**
     * 댓글 리스트 조회
     * @param postId
     * @return
     */
    @GetMapping("/posts/{postId}/comments")
    public PagingResponse<CommentResponse> getList(@PathVariable Long postId, @ModelAttribute CommentSearch commentSearch) {
        return commentService.getList(postId, commentSearch);
    }

    /**
     * 댓글 삭제
     * ( HTTP DELETE 을 쓰지 않은 이유는 RequestBody 값이 무시되는 경우가 있어 POST 메소드로 씀 )
     * @param commentId
     * @param request
     */
    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable Long commentId, @RequestBody @Valid CommentDelete request) {
        commentService.delete(commentId, request);
    }

}
