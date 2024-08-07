package ru.skillbox.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.commonlib.dto.account.Role;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "users")
@Accessors(chain = true)
@ToString(exclude = {"friendsFrom", "friendsTo"})
@Builder
@Getter
@Setter
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
    private ZonedDateTime regDate;

    @Column(name = "birth_date")
    private ZonedDateTime birthDate;

    @Column(name = "message_permission")
    private String messagePermission;

    @Column(name = "last_online_time")
    private ZonedDateTime lastOnlineTime;

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
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    private String password;

    @JsonIgnoreProperties("friendsFrom")
    @JsonIgnore
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "account_id_from"),
            inverseJoinColumns = @JoinColumn(name = "account_id_to")
    )
    private List<User> friendsFrom = new ArrayList<>();

    @JsonIgnoreProperties("friendsTo")
    @JsonIgnore
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "account_id_to"),
            inverseJoinColumns = @JoinColumn(name = "account_id_from")
    )
    private List<User> friendsTo = new ArrayList<>();

    public Set<User> getFriends() {
        return Stream.concat(friendsFrom.stream(), friendsTo.stream())
                .collect(toSet());
    }
}
