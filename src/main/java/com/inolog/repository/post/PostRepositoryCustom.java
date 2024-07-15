package com.inolog.repository.post;

import com.inolog.domain.Post;
import com.inolog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
