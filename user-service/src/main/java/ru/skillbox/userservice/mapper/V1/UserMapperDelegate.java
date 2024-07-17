package ru.skillbox.userservice.mapper.V1;

import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.service.FriendshipService;

import java.util.Set;

public abstract class UserMapperDelegate implements UserMapperV1 {
    @Autowired
    private FriendshipService friendshipService;

    @Override
    public User requestToUser(Long authUserId, AccountDto request) {

        return User.builder()
                .id(authUserId)
                .about(request.getAbout())
                .birthDate(request.getBirthDate())
                .city(request.getCity())
                .country(request.getCountry())
                .createdOn(request.getCreatedOn())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .photo(request.getPhoto())
                .photoId(request.getPhotoId())
                .isBlocked(request.isBlocked())
                .isDeleted(request.isDeleted())
                .isOnline(request.isOnline())
                .role(request.getRole())
                .messagePermission(request.getMessagePermission())
                .friends((Set<User>) friendshipService.getFriends(authUserId))
                .password(request.getPassword())
                .build();
    }

    @Override
    public AccountDto userToResponse(Long authUserId, User user) {
        return AccountDto.builder()
                .id(user.getId())
                .about(user.getAbout())
                .birthDate(user.getBirthDate())
                .city(user.getCity())
                .country(user.getCountry())
                .createdOn(user.getCreatedOn())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .photo(user.getPhoto())
                .photoId(user.getPhotoId())
                .isBlocked(user.isBlocked())
                .isDeleted(user.isDeleted())
                .isOnline(user.isOnline())
                .role(user.getRole())
                .messagePermission(user.getMessagePermission())
                .statusCode(friendshipService.getStatusCode(authUserId, user.getId()))
                .lastOnlineTime(user.getLastOnlineTime())
                .password(user.getPassword())
                .build();
    }
}
