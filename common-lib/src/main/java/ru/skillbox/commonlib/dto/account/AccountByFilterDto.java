package ru.skillbox.commonlib.dto.account;

import lombok.Data;

@Data
public class AccountByFilterDto {
    AccountSearchDto accountSearchDto;
    int pageSize;
    int pageNumber;
}
