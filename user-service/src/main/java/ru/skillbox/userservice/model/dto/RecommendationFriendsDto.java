package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RecommendationFriendsDto {
    private Long id;
    private String photo;
    private String firstName;
    private String lastName;
}
