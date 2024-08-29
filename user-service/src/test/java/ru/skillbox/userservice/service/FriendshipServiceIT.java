package ru.skillbox.userservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.commonlib.dto.account.Role;
import ru.skillbox.commonlib.dto.account.StatusCode;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.entity.FriendshipId;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.FriendshipRepository;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class FriendshipServiceIT {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setEmail("testUser1@gmail.com");
        user1.setRole(Role.USER);
        user1.setFriendsFrom(Collections.emptyList());
        user1.setFriendsTo(Collections.emptyList());
        userRepository.save(user1);

        user2 = new User();
        user2.setRole(Role.USER);
        user2.setFriendsFrom(Collections.emptyList());
        user2.setFriendsTo(Collections.emptyList());
        user2.setEmail("testUser2@gmail.com");
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Request friendship with valid request should create friendship")
    void requestFriendship_validRequest_friendshipCreated() {
        friendshipService.requestFriendship(user1.getId(), user2.getId());

        assertEquals(StatusCode.REQUEST_TO, friendshipRepository.findById(new FriendshipId(user1.getId(), user2.getId())).get().getStatusCode());
        assertEquals(StatusCode.REQUEST_FROM, friendshipRepository.findById(new FriendshipId(user2.getId(), user1.getId())).get().getStatusCode());
    }

    @Test
    @DisplayName("Approve friendship with valid request should approve friendship")
    void approveFriendship_validRequest_friendshipApproved() {
        friendshipService.requestFriendship(user1.getId(), user2.getId());
        friendshipService.approveFriendship(user2.getId(), user1.getId());

        assertEquals(StatusCode.FRIEND, friendshipRepository.findById(new FriendshipId(user1.getId(), user2.getId())).get().getStatusCode());
        assertEquals(StatusCode.FRIEND, friendshipRepository.findById(new FriendshipId(user2.getId(), user1.getId())).get().getStatusCode());
    }

    @Test
    @DisplayName("Delete friendship with valid request should remove friendship")
    void deleteFriendship_validRequest_friendshipDeleted() {
        friendshipService.requestFriendship(user1.getId(), user2.getId());
        friendshipService.deleteFriendship(user1.getId(), user2.getId());

        assertFalse(friendshipRepository.findById(new FriendshipId(user1.getId(), user2.getId())).isPresent());
        assertFalse(friendshipRepository.findById(new FriendshipId(user2.getId(), user1.getId())).isPresent());
    }

    @Test
    @DisplayName("Get friend request count with no requests should return zero")
    void getFriendRequestCount_noRequests_returnsZero() {
        int requestCount = friendshipService.getFriendRequestCount(user1.getId());

        assertEquals(0, requestCount);
    }

    @Test
    @DisplayName("Block account with valid request should block account")
    void blockAccount_validRequest_accountBlocked() {
        friendshipService.requestFriendship(user1.getId(), user2.getId());
        friendshipService.blockAccount(user1.getId(), user2.getId());

        assertEquals(StatusCode.BLOCKED, friendshipRepository.findById(new FriendshipId(user1.getId(), user2.getId())).get().getStatusCode());
        assertEquals(StatusCode.REJECTING, friendshipRepository.findById(new FriendshipId(user2.getId(), user1.getId())).get().getStatusCode());
    }

    @Test
    @DisplayName("Subscribe to account should set correct statuses")
    void subscribeToAccount_validRequest_statusSetCorrectly() {
        friendshipService.subscribeToAccount(user1.getId(), user2.getId());

        assertEquals(StatusCode.WATCHING, friendshipRepository.findById(new FriendshipId(user1.getId(), user2.getId())).get().getStatusCode());
        assertEquals(StatusCode.SUBSCRIBED, friendshipRepository.findById(new FriendshipId(user2.getId(), user1.getId())).get().getStatusCode());
    }

    @Test
    @DisplayName("Get friend request count with one request should return one")
    void getFriendRequestCount_oneRequest_returnsOne() {
        friendshipService.requestFriendship(user1.getId(), user2.getId());
        int requestCount = friendshipService.getFriendRequestCount(user1.getId());

        assertEquals(1, requestCount);
    }
    @Test
    @DisplayName("Get friends by status should return friends with the specified status")
    void getFriendsByStatus_validRequest_returnsFriendsByStatus() {
        Long userId = user1.getId();
        User friend1 = new User();
        friend1.setEmail("friend1@gmail.com");
        friend1.setRole(Role.USER);
        userRepository.save(friend1);

        User friend2 = new User();
        friend2.setEmail("friend2@gmail.com");
        friend2.setRole(Role.USER);
        userRepository.save(friend2);
        friendshipService.requestFriendship(user1.getId(), friend1.getId());
        friendshipService.approveFriendship(friend1.getId(), user1.getId());

        friendshipService.requestFriendship(user1.getId(), friend2.getId());
        friendshipService.blockAccount(user1.getId(), friend2.getId());
        Page<FriendDto> friendsWithStatusFriend = friendshipService.getFriendsByStatus(StatusCode.FRIEND, 10, userId);
        Page<FriendDto> friendsWithStatusBlocked = friendshipService.getFriendsByStatus(StatusCode.BLOCKED, 10, userId);
        assertEquals(1, friendsWithStatusFriend.getTotalElements());
        assertEquals(1, friendsWithStatusBlocked.getTotalElements());
    }
}