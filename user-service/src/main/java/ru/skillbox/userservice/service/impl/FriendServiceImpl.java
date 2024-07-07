package ru.skillbox.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.model.dto.AllFriendsDto;
import ru.skillbox.userservice.model.dto.FriendSearchDto;
import ru.skillbox.userservice.model.dto.RecommendationFriendsDto;
import ru.skillbox.userservice.model.dto.StatusCode;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;
import ru.skillbox.userservice.service.FriendService;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public void deleteFriend(Long currentAuthUserId, Long accountId) {
        setFriendship(currentAuthUserId, accountId, StatusCode.NONE);
        setFriendship(accountId, currentAuthUserId, StatusCode.NONE);
    }

    @Override
    public void approveFriend(Long currentAuthUserId, Long accountId) {
    }

    @Override
    public void blockFriend(Long currentAuthUserId, Long accountId) {
    }

    @Override
    public void addFriend(Long currentAuthUserId, Long accountId) {
        var accountFrom = userRepository.findById(currentAuthUserId).orElseThrow();
        var accountTo = userRepository.findById(accountId).orElseThrow();
        if (!accountFrom.getFriends().contains(accountTo)) {
            accountFrom.getFriends().add(accountTo);
            accountTo.getFriends().add(accountFrom);
        }
        userRepository.save(accountFrom);

        setFriendship(currentAuthUserId, accountId, StatusCode.REQUEST_TO);
        setFriendship(accountId, currentAuthUserId, StatusCode.REQUEST_FROM);
    }

    @Override
    public void subscribe(Long currentAuthUserId, Long accountId) {
    }

    @Override
    public AllFriendsDto getFriends(Long currentAuthUserId, FriendSearchDto friendSearchDto) {
        return null;
    }

    @Override
    public void getFriendByAccountDto(Long currentAuthUserId, Long accountId) {
    }

    @Override
    public RecommendationFriendsDto getByRecommendation(Long currentAuthUserId) {
        return null;
    }

    @Override
    public void getFriendId(Long currentAuthUserId) {
    }

    @Override
    public void requestCount(Long currentAuthUserId) {
    }

    @Override
    public void getBlockFriendId(Long currentAuthUserId) {
    }

    private void setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        var friendshipFrom = friendshipRepository.findById(new FriendshipId(accountIdFrom, accountIdTo))
                .orElseThrow();
        friendshipFrom.setStatusCode(statusCode);
        friendshipRepository.save(friendshipFrom);
    }
}
