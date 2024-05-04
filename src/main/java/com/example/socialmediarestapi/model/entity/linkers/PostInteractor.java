package com.example.socialmediarestapi.model.entity.linkers;

import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.model.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostInteractor implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JsonIgnore
    private Profile profile;


}
