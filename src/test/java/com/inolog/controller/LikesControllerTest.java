package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Likes;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.repository.likes.LikesRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LikesControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikesRepository likesRepository;

    @AfterEach
    public void clean() {
        likesRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @InologMockUser
    @DisplayName("게시글 좋아요")
    void test1() throws Exception {
        // given
        User user = userRepository.findAll().get(0);
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .user(user)
                .build();
        postRepository.save(post);

        // when
        mockMvc.perform(post("/posts/likes/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Assertions.assertEquals(likesRepository.countByPostId(post.getId()), 1);
    }

    @Test
    @InologMockUser
    @DisplayName("게시글 좋아요 헤제")
    void test2() throws Exception {
        // given
        User user = userRepository.findAll().get(0);
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .user(user)
                .build();
        postRepository.save(post);

        Likes likes = Likes.builder()
                .user(user)
                .post(post)
                .build();
        likesRepository.save(likes);

        // when
        mockMvc.perform(delete("/posts/likes/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Assertions.assertEquals(likesRepository.countByPostId(post.getId()), 0);
    }
}