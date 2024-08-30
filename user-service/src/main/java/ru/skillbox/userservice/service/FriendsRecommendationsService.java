package ru.skillbox.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.model.dto.RecommendedFriendDto;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendsRecommendationsService {

    private final UserRepository userRepository;

    public List<RecommendedFriendDto> recommendFriends(Long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow();
        List<User> potentialFriends = findPotentialFriends(currentUser);
        return generateRecommendations(currentUser, potentialFriends);
    }

    public List<User> findPotentialFriends(User currentUser) {
        if(currentUser.getFriendsFrom() == null) {
            currentUser.setFriendsFrom(new ArrayList<>());
        }
        if(currentUser.getFriendsTo() == null) {
            currentUser.setFriendsTo(new ArrayList<>());
        }
        List<User> allUsers = userRepository.findAllByIsDeletedAndIdNot(Pageable.unpaged(), false, currentUser.getId());
        return allUsers.stream()
                .filter(user -> !currentUser.getFriends().contains(user))
                .toList();
    }

    public List<RecommendedFriendDto> generateRecommendations(User currentUser, List<User> potentialFriends) {
        List<RecommendedFriendDto> recommendations = new ArrayList<>();
        for (User potentialFriend : potentialFriends) {
            int rating = calculateRating(currentUser, potentialFriend);
            if (rating > 0) {
                recommendations.add(createRecommendedFriendDto(potentialFriend, rating));
            }
        }
        return recommendations.stream()
                .sorted(Comparator.comparingInt(RecommendedFriendDto::getRating).reversed())
                .toList();
    }

    public int calculateRating(User currentUser, User potentialFriend) {
        int rating = 0;
        if (currentUser.getCity() != null && currentUser.getCity().equals(potentialFriend.getCity())) {
            rating++;
        }
        if (currentUser.getCountry() != null && currentUser.getCountry().equals(potentialFriend.getCountry())) {
            rating++;
        }
        if (currentUser.getBirthDate() != null && isAgeClose(currentUser, potentialFriend)) {
            rating++;
        }
        if (hasCommonFriends(currentUser, potentialFriend)) {
            rating++;
        }
        return rating;
    }

    public boolean isAgeClose(User user1, User user2) {
        int age1 = calculateAge(user1.getBirthDate());
        int age2 = calculateAge(user2.getBirthDate());
        return Math.abs(age1 - age2) <= 2;
    }

    public int calculateAge(ZonedDateTime birthDate) {
        return ZonedDateTime.now().getYear() - birthDate.getYear();
    }

    public boolean hasCommonFriends(User user1, User user2) {
        Set<User> friendsUser1 = new HashSet<>(user1.getFriends());
        Set<User> friendsUser2 = new HashSet<>(user2.getFriends());
        friendsUser1.retainAll(friendsUser2);
        return !friendsUser1.isEmpty();
    }

    public RecommendedFriendDto createRecommendedFriendDto(User user, int rating) {
        return RecommendedFriendDto.builder()
                .id(user.getId())
                .isDeleted(user.isDeleted())
                .friendId(user.getId())
                .rating(rating)
                .build();
    }
}