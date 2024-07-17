package com.inolog.repository.post;

import com.inolog.domain.Post;
import com.inolog.request.post.PostSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> getList(PostSearch postSearch);
}
