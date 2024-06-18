package ru.skillbox.userservice.model.dto;

import lombok.Data;

@Data
public class AccountByFilterDto {
    AccountSearchDto accountSearchDto;
    int pageSize;
    int pageNumber;
}
