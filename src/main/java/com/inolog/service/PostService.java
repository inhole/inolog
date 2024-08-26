package com.inolog.service;

import com.inolog.domain.Post;
import com.inolog.domain.PostEditor;
import com.inolog.exception.CategoryNotFound;
import com.inolog.exception.PostNotFound;
import com.inolog.exception.UserNotFound;
import com.inolog.repository.post.PostRepository;
import com.inolog.repository.user.UserRepository;
import com.inolog.request.post.PostCreate;
import com.inolog.request.post.PostEdit;
import com.inolog.request.post.PostSearch;
import com.inolog.response.PagingResponse;
import com.inolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void write(Long userId, PostCreate postCreate) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        if (postCreate.getCategory().getId() == null || postCreate.getCategory().getId() == 0) {
            throw new CategoryNotFound();
        }

        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .category(postCreate.getCategory())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return new PostResponse(post);
    }

    public PagingResponse<PostResponse> getList(PostSearch postSearch) {
        Page<Post> postPage = postRepository.getList(postSearch);
        PagingResponse<PostResponse> postList = new PagingResponse<>(postPage, PostResponse.class);

        return postList;
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }

    @Transactional
    public void updateLike(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        post.updateLike();
    }
}
