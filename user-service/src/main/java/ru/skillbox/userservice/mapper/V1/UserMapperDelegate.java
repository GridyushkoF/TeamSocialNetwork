package ru.skillbox.userservice.mapper.V1;

import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.StatusCode;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;

public abstract class UserMapperDelegate implements UserMapperV1 {
    @Autowired
    private FriendshipRepository friendshipRepository;

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
                .statusCode(getStatusCode(authUserId, user.getId()))
                .lastOnlineTime(user.getLastOnlineTime())
                .password(user.getPassword())
                .build();
    }

    private StatusCode getStatusCode(Long authUserId, Long id) {
        try {
            FriendshipId friendshipId = new FriendshipId(authUserId, id);
            Friendship friendshipFrom = friendshipRepository.findById(friendshipId)
                    .orElse(new Friendship(friendshipId));
            return friendshipFrom.getStatusCode();
        } catch (Exception e) {
            return StatusCode.NONE;
        }
    }
}
