package ru.skillbox.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import ru.skillbox.userservice.model.dto.RecommendedFriendDto;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendsRecommendationsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendsRecommendationsService friendsRecommendationsService;

    private User currentUser;
    private User potentialFriend1;
    private User potentialFriend2;
    private User potentialFriend3;

    @BeforeEach
    void setUp() {
        currentUser = createUser(1L, "Moscow", "Russia", ZonedDateTime.now().minusYears(30));
        potentialFriend1 = createUser(2L, "Moscow", "Russia", ZonedDateTime.now().minusYears(28));
        potentialFriend2 = createUser(3L, "London", "UK", ZonedDateTime.now().minusYears(30));
        potentialFriend3 = createUser(4L, "Moscow", "Russia", ZonedDateTime.now().minusYears(35));
    }

    @Test
    void testRecommendFriends() {
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(userRepository.findAllByIsDeletedAndIdNot(Pageable.unpaged(), false, currentUser.getId()))
                .thenReturn(List.of(potentialFriend1, potentialFriend2, potentialFriend3));

        List<RecommendedFriendDto> recommendations = friendsRecommendationsService.recommendFriends(currentUser.getId());

        assertEquals(3, recommendations.size());
        assertEquals(potentialFriend1.getId(), recommendations.get(0).getFriendId());
        assertEquals(3, recommendations.get(0).getRating());
        assertEquals(potentialFriend3.getId(), recommendations.get(1).getFriendId());
        assertEquals(2, recommendations.get(1).getRating());
        assertEquals(potentialFriend2.getId(), recommendations.get(2).getFriendId());
        assertEquals(1, recommendations.get(2).getRating());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testFindPotentialFriends() {
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(userRepository.findAllByIsDeletedAndIdNot(Pageable.unpaged(), false, currentUser.getId()))
                .thenReturn(List.of(potentialFriend1, potentialFriend2, potentialFriend3));

        List<User> potentialFriends = friendsRecommendationsService.findPotentialFriends(currentUser);

        assertEquals(3, potentialFriends.size());
        assertTrue(potentialFriends.contains(potentialFriend1));
        assertTrue(potentialFriends.contains(potentialFriend2));
        assertTrue(potentialFriends.contains(potentialFriend3));
    }

    @Test
    void testGenerateRecommendations() {
        List<User> potentialFriends = List.of(potentialFriend1, potentialFriend2, potentialFriend3);
        List<RecommendedFriendDto> recommendations = friendsRecommendationsService.generateRecommendations(currentUser, potentialFriends);

        assertEquals(3, recommendations.size());
        assertEquals(potentialFriend1.getId(), recommendations.get(0).getFriendId());
        assertEquals(3, recommendations.get(0).getRating());
        assertEquals(potentialFriend3.getId(), recommendations.get(1).getFriendId());
        assertEquals(2, recommendations.get(1).getRating());
    }

    @Test
    void testCalculateRating() {
        int rating1 = friendsRecommendationsService.calculateRating(currentUser, potentialFriend1);
        int rating2 = friendsRecommendationsService.calculateRating(currentUser, potentialFriend2);
        int rating3 = friendsRecommendationsService.calculateRating(currentUser, potentialFriend3);

        assertEquals(3, rating1);
        assertEquals(1, rating2);
        assertEquals(2, rating3);
    }

    @Test
    void testIsAgeClose() {
        boolean result1 = friendsRecommendationsService.isAgeClose(currentUser, potentialFriend1);
        boolean result2 = friendsRecommendationsService.isAgeClose(currentUser, potentialFriend3);
        boolean result3 = friendsRecommendationsService.isAgeClose(currentUser, potentialFriend2);

        assertTrue(result1);
        assertFalse(result2);
        assertTrue(result3);
    }

    @Test
    void testCalculateAge() {
        int age = friendsRecommendationsService.calculateAge(currentUser.getBirthDate());
        int expectedAge = 30;
        assertEquals(expectedAge, age);
    }

    @Test
    void testHasCommonFriends() {
        User friend1 = createUser(5L, "Moscow", "Russia", ZonedDateTime.now().minusYears(29));
        User friend2 = createUser(6L, "Moscow", "Russia", ZonedDateTime.now().minusYears(27));

        currentUser.setFriendsFrom(List.of(friend1));
        potentialFriend1.setFriendsFrom(List.of(friend1, friend2));

        boolean hasCommon = friendsRecommendationsService.hasCommonFriends(currentUser, potentialFriend1);
        boolean noCommon = friendsRecommendationsService.hasCommonFriends(currentUser, potentialFriend2);

        assertTrue(hasCommon);
        assertFalse(noCommon);
    }

    @Test
    void testCreateRecommendedFriendDto() {
        RecommendedFriendDto dto = friendsRecommendationsService.createRecommendedFriendDto(potentialFriend1, 3);

        assertEquals(potentialFriend1.getId(), dto.getId());
        assertFalse(dto.isDeleted());
        assertEquals(potentialFriend1.getId(), dto.getFriendId());
        assertEquals(3, dto.getRating());
    }

    private User createUser(Long id, String city, String country, ZonedDateTime birthDate) {
        return User.builder()
                .id(id)
                .city(city)
                .country(country)
                .birthDate(birthDate)
                .friendsFrom(new ArrayList<>())
                .friendsTo(new ArrayList<>())
                .build();
    }
}