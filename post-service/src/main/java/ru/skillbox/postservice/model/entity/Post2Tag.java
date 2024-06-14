package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post2tag")
public class Post2Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "post_id", columnDefinition = "INT NOT NULL")
    private int postId;

    @Column(name = "tag_id", columnDefinition = "INT NOT NULL")
    private int tagId;
}
