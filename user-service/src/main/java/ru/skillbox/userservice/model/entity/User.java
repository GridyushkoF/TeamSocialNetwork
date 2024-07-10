package ru.skillbox.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.commondto.account.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Accessors(chain = true)
@ToString(exclude = "friends")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String email;

    private String phone;

    private String photo;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    private String city;

    private String country;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "message_permission")
    private String messagePermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_online")
    private boolean isOnline;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    private String password;

    @JsonIgnoreProperties("friends")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "account_id_from",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "account_id_to",
                    referencedColumnName = "id"))
    private Set<User> friends;
}
