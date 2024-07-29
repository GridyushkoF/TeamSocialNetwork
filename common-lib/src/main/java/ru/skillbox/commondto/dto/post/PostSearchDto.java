package ru.skillbox.commondto.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean withFriends;
    private boolean isDelete;
    private List<String> tags;
    private Long dateFrom;
    private Long dateTo;
}
