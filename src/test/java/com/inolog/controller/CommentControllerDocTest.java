package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Comment;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.repository.user.UserRepository;
import com.inolog.repository.comment.CommentRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.request.comment.CommentCreate;
import com.inolog.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.ino.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @InologMockUser
    @DisplayName("댓글 등록")
    void test1() throws Exception {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .user(user)
                .build();
        postRepository.save(post);

        CommentCreate commentCreate = CommentCreate.builder()
                .author("작성자 입니다.")
                .password("123456")
                .content("댓글 내용입니다...")
                .build();
        String json = objectMapper.writeValueAsString(commentCreate);

        // expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-create", pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("author").description("작성자")
                                        .attributes(key("constraint").value("작성자는 1~8글자로 입력해주세요.")),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("비밀번호는 6~30글자로 입력해주세요.")),
                                fieldWithPath("content").description("내용")
                                        .attributes(key("constraint").value("내용은 10~1000자까지 입력해주세요."))
                        )
                ));
    }

    @Test
    @InologMockUser
    @DisplayName("댓글 리스트 조회")
    void test2() throws Exception {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .user(user)
                .build();
        postRepository.save(post);

        List<Comment> requestList = IntStream.range(0, 10).mapToObj(i ->
                Comment.builder()
                        .author("작성자" + i)
                        .password("123456")
                        .content("내용" + i)
                        .post(post)
                        .build()
        ).collect(Collectors.toList());
        commentRepository.saveAll(requestList);

        // expected
        mockMvc.perform(get("/posts/{postId}/comments?page=1&size=3", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-list",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("댓글 개수")
                        ),
                        responseFields(
                                fieldWithPath("page").description("페이지 번호"),
                                fieldWithPath("size").description("댓글 개수"),
                                fieldWithPath("totalCount").description("전체 댓글 개수"),
                                fieldWithPath("items").description("댓글 리스트"),
                                fieldWithPath("items[].id").description("댓글 ID"),
                                fieldWithPath("items[].author").description("작성자"),
                                fieldWithPath("items[].content").description("내용"),
                                fieldWithPath("items[].regDate").description("작성일")
                        )
                ));
    }

    @Test
    @InologMockUser
    @DisplayName("댓글 삭제")
    void test3() throws Exception {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .user(user)
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .author("작성자 입니다.")
                .password(passwordEncoder.encode("123456"))
                .content("내용 입니다....")
                .post(post)
                .build();
        commentRepository.save(comment);

        CommentDelete commentDelete = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(commentDelete);

        // expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-delete",
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("password").description("비밀번호")
                        )
                ));
    }

}





