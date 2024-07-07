package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccountByFilterDto {
    AccountSearchDto accountSearchDto;
    int pageSize;
    int pageNumber;
}
