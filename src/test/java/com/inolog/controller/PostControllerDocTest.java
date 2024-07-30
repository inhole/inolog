package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.UserRepository;
import com.inolog.request.post.PostCreate;
import com.inolog.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
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
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @InologMockUser
    @DisplayName("글 등록")
    void test1() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("ino")
                .content("content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON) // request 타입
                        .accept(APPLICATION_JSON) // response 타입
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("좋은제목 입력해주세요.")),
                                fieldWithPath("content").description("내용").optional()
                        )
                ));
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("regDate").description("작성일")
                        )
                ));
    }

    @Test
    @InologMockUser
    @DisplayName("글 여러개 조회")
    void test3() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        List<Post> requestPosts = IntStream.range(0, 10)
                .mapToObj(i -> Post.builder()
                        .title("title" + i)
                        .content("content" + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        this.mockMvc.perform(get("/posts?page=1&size=3")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-list", queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("글 개수")
                        ),
                        responseFields(
                                fieldWithPath("page").description("페이지 번호"),
                                fieldWithPath("size").description("글 개수"),
                                fieldWithPath("totalCount").description("전체 글 개수"),
                                fieldWithPath("items").description("게시글 리스트"),
                                fieldWithPath("items[].id").description("게시글 ID"),
                                fieldWithPath("items[].title").description("제목"),
                                fieldWithPath("items[].content").description("내용"),
                                fieldWithPath("items[].regDate").description("작성일")
                        )
                ));
    }

    @Test
    @InologMockUser
    @DisplayName("글 수정")
    void test4() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .user(user)
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("edit title")
                .content("edit content")
                .build();
        String json = objectMapper.writeValueAsString(postEdit);

        // expected
        this.mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON) // request 타입
                        .accept(APPLICATION_JSON) // response 타입
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-edit", pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )
                ));
    }

    @Test
    @InologMockUser
    @DisplayName("글 삭제")
    void test5() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .user(user)
                .build();

        postRepository.save(post);

        // expected
        this.mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON) // request 타입
                        .accept(APPLICATION_JSON) // response 타입
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )
                ));
    }
}





