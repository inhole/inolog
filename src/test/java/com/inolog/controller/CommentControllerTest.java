package com.inolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inolog.annotation.InologMockUser;
import com.inolog.domain.Comment;
import com.inolog.domain.Post;
import com.inolog.domain.User;
import com.inolog.repository.UserRepository;
import com.inolog.repository.comment.CommentRepository;
import com.inolog.repository.post.PostRepository;
import com.inolog.request.comment.CommentCreate;
import com.inolog.request.post.PostCreate;
import com.inolog.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void clean() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }


    @Test
    @InologMockUser
    @DisplayName("댓글 작성")
    void test1() throws Exception {
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

        CommentCreate request = CommentCreate.builder()
                .author("이노")
                .password("123456")
                .content("댓글입니다. 10 글자 제한 ......")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("이노", savedComment.getAuthor());
        assertNotEquals("123456", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("123456", savedComment.getPassword()));
        assertEquals("댓글입니다. 10 글자 제한 ......", savedComment.getContent());
    }


}