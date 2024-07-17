package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post2tag")
public class Post2Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "post_id", columnDefinition = "BIGINT NOT NULL")
    private long postId;

    @Column(name = "tag_id", columnDefinition = "INT NOT NULL")
    private int tagId;


}
