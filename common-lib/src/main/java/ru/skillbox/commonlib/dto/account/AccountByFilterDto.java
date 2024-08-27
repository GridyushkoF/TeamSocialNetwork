package ru.skillbox.commonlib.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountByFilterDto {
    AccountSearchDto accountSearchDto;
    int pageSize;
    int pageNumber;
}
