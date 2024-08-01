package ru.skillbox.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.StatusCode;
import ru.skillbox.userservice.exception.NoFriendshipFoundException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.mapper.V1.FriendMapperV1;
import ru.skillbox.userservice.mapper.V1.UserMapperV1;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.Collections;
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

    @Transactional
    public void requestFriendship(Long currentAuthUserId, Long accountId) {
        log.info("Request friendship between accounts - id: {} and id: {}", currentAuthUserId, accountId);

        saveAccountFriends(currentAuthUserId, accountId);

        setFriendship(currentAuthUserId, accountId, StatusCode.REQUEST_TO);
        setFriendship(accountId, currentAuthUserId, StatusCode.REQUEST_FROM);
    }

    private void setFriendship(Long accountIdFrom, Long accountIdTo, StatusCode statusCode) {
        Friendship friendship = friendshipRepository.findByAccountIdFromAndAccountIdTo(accountIdFrom, accountIdTo)
                .orElseThrow(() -> new NoFriendshipFoundException("Accounts id: " + accountIdFrom + " and id: " + accountIdTo + " have no relationships"));
        friendship.setStatusCode(statusCode);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void deleteFriendship(Long currentAuthUserId, Long accountId) {
        log.info("Delete friendship between accounts - id: {} and id: {}", currentAuthUserId, accountId);
        Friendship friendshipFrom = friendshipRepository.findByAccountIdFromAndAccountIdTo(currentAuthUserId, accountId)
                .orElseThrow(() ->
                        new NoFriendshipFoundException("Accounts id: " + currentAuthUserId + " and id: " + accountId + " have no relationships"));
        friendshipRepository.delete(friendshipFrom);
        Friendship friendshipTo = friendshipRepository.findByAccountIdFromAndAccountIdTo(accountId, currentAuthUserId)
                .orElseThrow(() ->
                        new NoFriendshipFoundException("Accounts id: " + accountId + " and id: " + currentAuthUserId + " have no relationships"));
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

    public List<AccountDto> getFriendRecommendations(Long currentAuthUserId) {
        User currentUser = userRepository.findById(currentAuthUserId)
                .orElseThrow(() ->
                        new NoSuchAccountException("Account with id: " + currentAuthUserId + " does not exists")
                );
        Set<User> currentFriends = currentUser.getFriends();

        List<User> allUsers = userRepository.findAll();
        List<AccountDto> allPossibleFriends = allUsers.stream()
                .filter(user -> !user.getId().equals(currentAuthUserId))
                .filter(user -> !currentFriends.contains(user))
                .filter(user -> !Collections.disjoint(currentFriends, user.getFriends()))
                .map(user -> userMapper.userToResponse(currentAuthUserId, user))
                .toList();

        log.info("Get possible friends of userId:{} {}", currentAuthUserId, allPossibleFriends);

        return allPossibleFriends;
    }

    public int getFriendRequestCount(Long currentAuthUserId) {
        User currentUser = userRepository.findById(currentAuthUserId)
                .orElseThrow(() ->
                        new NoSuchAccountException("Account with id: " + currentAuthUserId + " does not exists")
                );
        return currentUser.getFriends().stream()
                .map(user -> userMapper.userToResponse(currentAuthUserId, user))
                .filter(accountDto -> accountDto.getStatusCode().equals(StatusCode.REQUEST_TO))
                .toList().size();
    }

    private void saveAccountFriends(Long currentAuthUserId, Long accountId) {
        User accountFrom = userRepository.findById(currentAuthUserId)
                .orElseThrow(() ->
                        new NoSuchAccountException("Account with id: " + currentAuthUserId + " does not exists")
                );
        User accountTo = userRepository.findById(accountId)
                .orElseThrow(() ->
                        new NoSuchAccountException("Account with id: " + accountId + " does not exists")
                );
        if (!accountFrom.getFriendsFrom().contains(accountTo)) {
            accountFrom.getFriendsFrom().add(accountTo);
        }
        if (!accountFrom.getFriendsTo().contains(accountTo)) {
            accountFrom.getFriendsTo().add(accountTo);
        }
        userRepository.save(accountFrom);
    }


    public Page<FriendDto> getFriendsByStatus(StatusCode statusCode, int size, Long currentAuthUserId) {

        User currentUser = userRepository.findById(currentAuthUserId).orElseThrow();
        Pageable nextPage = PageRequest.of(0, size);
        Set<User> friends = currentUser.getFriends();
        List<FriendDto> accounts = friends
                .stream().map(user -> userMapper.userToResponse(currentAuthUserId, user))
                .filter(accountDto -> accountDto.getStatusCode().equals(statusCode))
                .map(friendMapper::accountToFriend)
                .skip(nextPage.getOffset())
                .limit(nextPage.getPageSize()).toList();

        log.info("Get friends of userId:{} with status {} {}", currentAuthUserId, statusCode, accounts);

        return new PageImpl<>(accounts, nextPage, accounts.size());
    }
}
