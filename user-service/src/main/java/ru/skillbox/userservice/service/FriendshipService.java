package ru.skillbox.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.commondto.account.StatusCode;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendshipService {

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

    private Friendship setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        Friendship friendshipFrom = friendshipRepository.findById(new FriendshipId(accountIdFrom, accountIdTo))
                .orElse(new Friendship(accountIdFrom, accountIdTo, statusCode));
        friendshipFrom.setStatusCode(statusCode);
        friendshipRepository.save(friendshipFrom);
        return friendshipFrom;
    }

    public void deleteFriendship(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.NONE);
        setFriendship(accountId, currentAuthUserId, StatusCode.NONE);
    }

    public StatusCode getStatusCode(Long accountIdFrom, Long accountIdTo) {
        Friendship friendshipFrom = friendshipRepository.findById(new FriendshipId(accountIdFrom, accountIdTo))
                .orElse(setFriendship(accountIdFrom, accountIdTo, StatusCode.NONE));
        return friendshipFrom.getStatusCode();
    }
    public Set<User> getFriends(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new NoSuchAccountException("Can't find Account with id: " + id))
                .getFriends();
    }
}
