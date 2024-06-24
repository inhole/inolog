package com.inolog.repository;

import com.inolog.domain.Post;
import com.inolog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
