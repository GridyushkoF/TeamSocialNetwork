package ru.skillbox.commonlib.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchDto {
    private List<Integer> ids;
    private List<Integer> accountIds;
    private List<Integer> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDeleted;
    private List<String> tags;
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
}
