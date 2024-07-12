package ru.skillbox.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.model.dto.StatusCode;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
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

    private void setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        Friendship friendshipFrom = friendshipRepository.findById(new FriendshipId(accountIdFrom, accountIdTo))
                .orElse(new Friendship(new FriendshipId(accountIdFrom, accountIdTo)));
        friendshipFrom.setStatusCode(statusCode);
        friendshipRepository.save(friendshipFrom);
    }

    public void deleteFriendship(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.NONE);
        setFriendship(accountId, currentAuthUserId, StatusCode.NONE);
    }

    public void approveFriendship(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.APPROVED);
        setFriendship(accountId, currentAuthUserId, StatusCode.APPROVED);
    }

    public void blockFriend(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.BLOCKED);
        setFriendship(accountId, currentAuthUserId, StatusCode.BLOCKED);
    }

    public void subscribeToFriend(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.SUBSCRIBED);
    }

    public List<User> getAllFriends(Long currentAuthUserId) {
        User user = userRepository.findById(currentAuthUserId).orElseThrow();
        return (List<User>) user.getFriends();
    }

    public User getFriendById(Long currentAuthUserId, Long accountId) {
        return userRepository.findById(accountId).orElseThrow();
    }

    public List<User> getFriendRecommendations(Long currentAuthUserId) {
        User currentUser = userRepository.findById(currentAuthUserId).orElseThrow();
        Set<User> currentFriends = currentUser.getFriends();

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getId() != currentAuthUserId)
                .filter(user -> !currentFriends.contains(user))
                .filter(user -> !Collections.disjoint(currentFriends, user.getFriends()))
                .toList();
    }

    public List<User> getFriends(Long currentAuthUserId) {
        User user = userRepository.findById(currentAuthUserId).orElseThrow();
        return (List<User>) user.getFriends();
    }

    public int getFriendRequestCount(Long currentAuthUserId) {
        return friendshipRepository.countByAccountIdFromAndStatusCode(currentAuthUserId, StatusCode.REQUEST_TO);
    }

    public List<Long> getBlockedFriendIds(Long currentAuthUserId) {
        List<Friendship> blockedFriendships = friendshipRepository.findByStatusCodeAndIdAccountIdFrom(StatusCode.BLOCKED, currentAuthUserId);
        return blockedFriendships.stream()
                .map(friendship -> friendship.getId().getAccountIdTo())
                .toList();
    }

}
