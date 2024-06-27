package ru.skillbox.commondto;

import lombok.Data;

@Data
public class AccountByFilterDto {
    AccountSearchDto accountSearchDto;
    int pageSize;
    int pageNumber;
}


