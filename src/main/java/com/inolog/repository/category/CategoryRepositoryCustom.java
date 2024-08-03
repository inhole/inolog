package com.inolog.repository.category;

import com.inolog.domain.Category;
import com.inolog.domain.Comment;
import com.inolog.request.comment.CommentSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<Category> getList();
}
