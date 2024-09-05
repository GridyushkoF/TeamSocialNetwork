package ru.skillbox.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.account.StatusCode;
import ru.skillbox.commonlib.event.notification.NotificationType;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.mapper.v1.FriendMapperV1;
import ru.skillbox.userservice.mapper.v1.UserMapperV1;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.dto.FriendShortDto;
import ru.skillbox.userservice.model.dto.RecommendedFriendDto;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.processor.FriendProcessor;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendMapperV1 friendMapper;
    private final UserMapperV1 userMapper;
    private final FriendProcessor friendProcessor;
    private final FriendsRecommendationsService friendsRecommendationsService;
    private static final String ACCOUNT_WITH_ID = "Account with id: ";
    private static final String DOES_NOT_EXISTS = " does not exists";
    @Transactional
    public void requestFriendship(Long currentAuthUserId, Long accountId) {
        log.info("Request friendship between accounts - id: {} and id: {}", currentAuthUserId, accountId);

        this.saveAccountFriends(currentAuthUserId, accountId);

        this.setFriendship(currentAuthUserId, accountId, StatusCode.REQUEST_TO);
        this.setFriendship(accountId, currentAuthUserId, StatusCode.REQUEST_FROM);

        friendProcessor.process(currentAuthUserId, accountId, NotificationType.FRIEND_REQUEST);
    }
    public void setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        FriendshipId friendshipId = new FriendshipId(accountIdFrom, accountIdTo);
        Friendship friendshipFrom = friendshipRepository.findById(friendshipId)
                .orElse(new Friendship(friendshipId));
        friendshipFrom.setStatusCode(statusCode);
        friendshipRepository.save(friendshipFrom);
    }

    @Transactional
    public void deleteFriendship(Long currentAuthUserId, Long accountId) {
        log.info("Delete friendship between accounts - id: {} and id: {}", currentAuthUserId, accountId);
        FriendshipId friendshipId = new FriendshipId(currentAuthUserId, accountId);
        Friendship friendshipFrom = friendshipRepository.findById(friendshipId)
                .orElse(new Friendship(friendshipId));

        friendshipRepository.delete(friendshipFrom);
        Friendship friendshipTo = friendshipRepository.findById(new FriendshipId(accountId, currentAuthUserId))
                .orElse(new Friendship(new FriendshipId(accountId, currentAuthUserId)));
        friendshipRepository.delete(friendshipTo);
    }

    @Transactional
    public void approveFriendship(Long currentAuthUserId, Long accountId) {
        log.info("Approve friendship between account id: {} and  account id: {}", currentAuthUserId, accountId);
        setFriendship(currentAuthUserId, accountId, StatusCode.FRIEND);
        setFriendship(accountId, currentAuthUserId, StatusCode.FRIEND);
    }

    @Transactional
    public void blockAccount(Long currentAuthUserId, Long accountId) {
        log.info("Block account id: {} ", accountId);
        setFriendship(currentAuthUserId, accountId, StatusCode.BLOCKED);
        setFriendship(accountId, currentAuthUserId, StatusCode.REJECTING);
    }

    @Transactional
    public void subscribeToAccount(Long currentAuthUserId, Long accountId) {
        log.info("Subscribe to account id: {}", accountId);

        saveAccountFriends(currentAuthUserId, accountId);

        setFriendship(accountId, currentAuthUserId, StatusCode.SUBSCRIBED);
        setFriendship(currentAuthUserId, accountId, StatusCode.WATCHING);
    }
    @Transactional
    public List<RecommendedFriendDto> getFriendRecommendations(Long currentAuthUserId) {
        return friendsRecommendationsService.recommendFriends(currentAuthUserId);

    }
    @Transactional
    public int getFriendRequestCount(Long currentAuthUserId) {
        User currentUser = userRepository.findById(currentAuthUserId)
                .orElseThrow(() ->
                        new NoSuchAccountException(ACCOUNT_WITH_ID + currentAuthUserId + DOES_NOT_EXISTS)
                );
        return currentUser.getFriends().stream()
                .map(user -> userMapper.userToResponse(currentAuthUserId, user))
                .filter(accountDto -> accountDto.getStatusCode().equals(StatusCode.REQUEST_TO))
                .toList().size();
    }
    public void saveAccountFriends(Long currentAuthUserId, Long accountId) {
        User accountFrom = userRepository.findById(currentAuthUserId)
                .orElseThrow(() ->
                        new NoSuchAccountException(ACCOUNT_WITH_ID + currentAuthUserId + DOES_NOT_EXISTS)
                );
        User accountTo = userRepository.findById(accountId)
                .orElseThrow(() ->
                        new NoSuchAccountException(ACCOUNT_WITH_ID + accountId + DOES_NOT_EXISTS)
                );

        accountFrom.getFriends().add(accountTo);

        userRepository.save(accountFrom);
    }

    @Transactional
    public Page<FriendShortDto> getFriendsByStatus(StatusCode statusCode, int size, Long currentAuthUserId) {

        User currentUser = userRepository.findById(currentAuthUserId).orElseThrow();
        Pageable nextPage = PageRequest.of(0, size);
        Set<User> friends = currentUser.getFriends();
        List<FriendShortDto> accounts = friends
                .stream().map(user -> userMapper.userToResponse(currentAuthUserId, user))
                .filter(accountDto -> accountDto.getStatusCode().equals(statusCode))
                .map(friendMapper::accountDtoToFriendShortDto)
                .skip(nextPage.getOffset())
                .limit(nextPage.getPageSize()).toList();

        log.info("Get friends of userId:{} with status {} {}", currentAuthUserId, statusCode, accounts);

        return new PageImpl<>(accounts, nextPage, accounts.size());
    }
}
