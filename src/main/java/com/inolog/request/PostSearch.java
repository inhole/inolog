package com.inolog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch {

    private int page;
    private int size;

    @Builder
    public PostSearch(int size, int page) {
        this.size = size;
        this.page = page;
    }
}
