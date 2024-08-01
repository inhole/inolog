package com.inolog.domain;

import com.inolog.request.category.CategoryEdit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Post> posts;

    @Builder
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void edit(CategoryEdit categoryEdit) {
        this.name = categoryEdit.getName();
    }
}
