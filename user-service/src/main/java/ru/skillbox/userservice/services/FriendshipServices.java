package ru.skillbox.userservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.model.dto.StatusCode;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class FriendshipServices {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public void requestFriendship(Long currentAuthUserId, Long accountId) {
        User accountFrom = userRepository.findById(currentAuthUserId).orElseThrow();
        User accountTo = userRepository.findById(accountId).orElseThrow();
        if (!accountFrom.getFriends().contains(accountTo)) {
            accountFrom.getFriends().add(accountTo);
            accountTo.getFriends().add(accountFrom);
        }
        userRepository.save(accountFrom);

        setFriendship(currentAuthUserId, accountId, StatusCode.REQUEST_TO);
        setFriendship(accountId, currentAuthUserId, StatusCode.REQUEST_FROM);
    }

    private void setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        Friendship friendshipFrom = friendshipRepository.findById(new FriendshipId(accountIdFrom, accountIdTo))
                .orElseThrow();
        friendshipFrom.setStatusCode(statusCode);
        friendshipRepository.save(friendshipFrom);
    }

    public void deleteFriendship(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.NONE);
        setFriendship(accountId, currentAuthUserId, StatusCode.NONE);
    }

}
