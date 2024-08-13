package ru.skillbox.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.commondto.account.StatusCode;
import ru.skillbox.commondto.notification.NotificationType;
import ru.skillbox.userservice.mapper.V1.FriendMapperV1;
import ru.skillbox.userservice.mapper.V1.UserMapperV1;
import ru.skillbox.userservice.model.entity.Friendship;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.processor.FriendProcessor;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  FriendshipRepository friendshipRepository;
    @Mock
    private  FriendMapperV1 friendMapper;
    @Mock
    private  UserMapperV1 userMapper;
    @Mock
    private  FriendProcessor processor;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User().setId(1L).setEmail("user1@example.com");
        user2 = new User().setId(2L).setEmail("user2@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(1L, 2L)).thenReturn(Optional.empty());
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(2L, 1L)).thenReturn(Optional.empty());
    }

    @Test
    void requestFriendship() {
        friendshipService.requestFriendship(1L, 2L);

        verify(friendshipRepository, times(2)).save(any(Friendship.class));
        verify(processor).process(1L, 2L, NotificationType.FRIEND_REQUEST);
    }
    @Test
    void deleteFriendship() {
        Long currentAuthUserId = 1L;
        Long accountId = 2L;

        Friendship friendshipFrom = new Friendship();
        friendshipFrom.setAccountIdFrom(currentAuthUserId);
        friendshipFrom.setAccountIdTo(accountId);
        Friendship friendshipTo = new Friendship();
        friendshipTo.setAccountIdFrom(accountId);
        friendshipTo.setAccountIdTo(currentAuthUserId);

        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(currentAuthUserId, accountId))
                .thenReturn(Optional.of(friendshipFrom));
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(accountId, currentAuthUserId))
                .thenReturn(Optional.of(friendshipTo));
        doNothing().when(friendshipRepository).delete(any(Friendship.class));

        friendshipService.deleteFriendship(currentAuthUserId, accountId);

        verify(friendshipRepository).findByAccountIdFromAndAccountIdTo(currentAuthUserId, accountId);
        verify(friendshipRepository).delete(friendshipFrom);
        verify(friendshipRepository).findByAccountIdFromAndAccountIdTo(accountId, currentAuthUserId);
        verify(friendshipRepository).delete(friendshipTo);
    }

    @Test
    void approveFriendship() {
        Long currentAuthUserId = 1L;
        Long accountId = 2L;
        StatusCode statusCode = StatusCode.FRIEND;

        Friendship friendshipFrom = new Friendship();
        friendshipFrom.setAccountIdFrom(currentAuthUserId);
        friendshipFrom.setAccountIdTo(accountId);
        Friendship friendshipTo = new Friendship();
        friendshipTo.setAccountIdFrom(accountId);
        friendshipTo.setAccountIdTo(currentAuthUserId);

        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(currentAuthUserId, accountId))
                .thenReturn(Optional.of(friendshipFrom));
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(accountId, currentAuthUserId))
                .thenReturn(Optional.of(friendshipTo));

        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> {
            Friendship updatedFriendship = invocation.getArgument(0);
            if (updatedFriendship.getAccountIdFrom().equals(currentAuthUserId)) {
                return friendshipFrom;
            } else {
                return friendshipTo;
            }
        });

        friendshipService.approveFriendship(currentAuthUserId, accountId);
        friendshipFrom.setStatusCode(statusCode);
        friendshipTo.setStatusCode(statusCode);

        verify(friendshipRepository, times(2)).findByAccountIdFromAndAccountIdTo(anyLong(), anyLong());
        verify(friendshipRepository, times(2)).save(any(Friendship.class));

        assertEquals(statusCode, friendshipFrom.getStatusCode());
        assertEquals(statusCode, friendshipTo.getStatusCode());
    }

    @Test
    void blockAccount() {
        Long currentAuthUserId = 1L;
        Long accountId = 2L;
        StatusCode blockedStatus = StatusCode.BLOCKED;
        StatusCode rejectingStatus = StatusCode.REJECTING;

        Friendship friendshipFrom = new Friendship();
        friendshipFrom.setAccountIdFrom(currentAuthUserId);
        friendshipFrom.setAccountIdTo(accountId);
        Friendship friendshipTo = new Friendship();
        friendshipTo.setAccountIdFrom(accountId);
        friendshipTo.setAccountIdTo(currentAuthUserId);

        FriendshipRepository friendshipRepository = mock(FriendshipRepository.class);
                UserRepository userRepository = mock(UserRepository.class);
                FriendMapperV1 friendMapper = mock(FriendMapperV1.class);
                UserMapperV1 userMapper = mock(UserMapperV1.class);
                FriendProcessor processor = mock(FriendProcessor.class);

        FriendshipService friendshipService = new FriendshipService(
                          userRepository,
                          friendshipRepository,
                          friendMapper,
                          userMapper,
                          processor
                  );
        
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(currentAuthUserId, accountId))
                .thenReturn(Optional.of(friendshipFrom));
        when(friendshipRepository.findByAccountIdFromAndAccountIdTo(accountId, currentAuthUserId))
                .thenReturn(Optional.of(friendshipTo));
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> {
            Friendship updatedFriendship = invocation.getArgument(0);
            if (updatedFriendship.getAccountIdFrom().equals(currentAuthUserId)) {
                updatedFriendship.setStatusCode(blockedStatus);
                return updatedFriendship;
            } else {
                updatedFriendship.setStatusCode(rejectingStatus);
                return updatedFriendship;
            }
        });

        friendshipService.blockAccount(currentAuthUserId, accountId);

        verify(friendshipRepository, times(2)).findByAccountIdFromAndAccountIdTo(anyLong(), anyLong());
        verify(friendshipRepository, times(2)).save(any(Friendship.class));

        assertEquals(blockedStatus, friendshipFrom.getStatusCode());
        assertEquals(rejectingStatus, friendshipTo.getStatusCode());
    }

    @Test
    void subscribeToAccount() {
        Long currentAuthUserId = 1L;
        Long accountId = 2L;
        StatusCode subscribedStatus = StatusCode.SUBSCRIBED;
        StatusCode watchingStatus = StatusCode.WATCHING;

        User accountFrom = new User();
        accountFrom.setId(currentAuthUserId);
        accountFrom.setFriendsFrom(new ArrayList<>());
        accountFrom.setFriendsTo(new ArrayList<>());

        User accountTo = new User();
        accountTo.setId(accountId);
        accountTo.setFriendsFrom(new ArrayList<>());
        accountTo.setFriendsTo(new ArrayList<>());

        when(userRepository.findById(currentAuthUserId)).thenReturn(Optional.of(accountFrom));
        when(userRepository.findById(accountId)).thenReturn(Optional.of(accountTo));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> invocation.getArgument(0));

        friendshipService.subscribeToAccount(currentAuthUserId, accountId);

        verify(userRepository, times(2)).findById(anyLong());
        verify(userRepository).save(accountFrom);
        verify(friendshipRepository, times(2)).findByAccountIdFromAndAccountIdTo(anyLong(), anyLong());
        verify(friendshipRepository, times(2)).save(any(Friendship.class));

        assertTrue(accountFrom.getFriendsFrom().contains(accountTo));
        assertTrue(accountFrom.getFriendsTo().contains(accountTo));
    }
}