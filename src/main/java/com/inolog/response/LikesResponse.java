package com.inolog.response;

import com.inolog.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikesResponse {

    private Long likesCount;
    private boolean likesYn;

    @Builder
    public LikesResponse(Long likesCount, boolean likesYn) {
        this.likesCount = likesCount;
        this.likesYn = likesYn;
    }
}
