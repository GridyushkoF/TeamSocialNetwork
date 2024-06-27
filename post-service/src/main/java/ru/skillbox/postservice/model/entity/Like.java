package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)

    private LikeEntityType entityType;
    @Column(name = "entity_id")
    private Long entityId;

}
