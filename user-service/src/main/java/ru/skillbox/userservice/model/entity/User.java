package ru.skillbox.userservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.Role;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
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
    private Role role;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    private String password;


    public static User of(AccountDto accountDto) {
        return new User()
                .setEmail(accountDto.getEmail())
                .setPhone(accountDto.getPhone())
                .setAbout(accountDto.getAbout())
                .setCity(accountDto.getCity())
                .setCountry(accountDto.getCountry())
                .setFirstName(accountDto.getFirstName())
                .setLastName(accountDto.getLastName())
                .setRegDate(accountDto.getRegDate())
                .setBirthDate(accountDto.getBirthDate())
                .setMessagePermission(accountDto.getMessagePermission())
                .setLastOnlineTime(accountDto.getLastOnlineTime())
                .setOnline(accountDto.isOnline())
                .setBlocked(accountDto.isBlocked())
                .setDeleted(accountDto.isDeleted())
                .setPhotoId(accountDto.getPhotoId())
                .setPhotoName(accountDto.getPhotoName())
                .setRole(accountDto.getRole())
                .setCreatedOn(accountDto.getCreatedOn())
                .setUpdatedOn(accountDto.getUpdatedOn())
                .setPassword(accountDto.getPassword());

    }
}