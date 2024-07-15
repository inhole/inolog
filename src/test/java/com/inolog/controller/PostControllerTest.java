package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.UserRepository;
import com.inolog.request.post.PostCreate;
import com.inolog.request.post.PostEdit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }


    @Test
    @InologMockUser
    @DisplayName("글 작성 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @InologMockUser
//    @WithMockUser(username = "sylee74133@gmail.com", roles = {"ADMIN"})
    @DisplayName("글 작성")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("타이틀임")
                .content("내용임")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals(post.getTitle(), "타이틀임");
        assertEquals(post.getContent(), "내용임");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .name("이인호")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("12345678901234")
                .content("bar")
                .user(user)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON) // application/json 주로 쓰임
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .name("이인호")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 19"))
                .andExpect(jsonPath("$[0].content").value("반포자이 19"))
                .andDo(print());

    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        User user = User.builder()
                .name("이인호")
                .email("sylee74133@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 19"))
                .andExpect(jsonPath("$[0].content").value("반포자이 19"))
                .andDo(print());
    }

    @Test
    @InologMockUser
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .user(user)
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @InologMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .user(user)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @InologMockUser
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // expected
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}