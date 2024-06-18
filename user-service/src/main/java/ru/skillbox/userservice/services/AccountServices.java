package ru.skillbox.userservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.exceptions.DefaultNotAuthException;
import ru.skillbox.userservice.model.dto.AccountByFilterDto;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.AccountRecoveryRq;
import ru.skillbox.userservice.model.dto.AccountSearchDto;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServices {

    //private final AccountRepository accountRepository;

    public String recoveryUserAccount(AccountRecoveryRq recoveryRequest) {
        // logic with DB
        return recoveryRequest.getEmail();
    }

    public AccountDto getUserAccount() {
        // logic with DB
        return new AccountDto();
    }

    public AccountDto updateUserAccount(AccountDto accountDto) {
        // logic with DB
        return new AccountDto();
    }

    public String deleteUserAccount(String userName) {
        if (userName == null) {
            throw new DefaultNotAuthException("No auth user found!");
        }
        // logic with DB
        return userName;
    }

    public String blockAccount(boolean block, long id) {
        if (block) {
            // logic with DB
            return "blocked";
        } else {
            // logic with DB
            return "unblocked";
        }
    }

    public List<AccountDto> getAllAccounts(Pageable page) {
        // logic with DB
        return List.of(new AccountDto());
    }

    public long createAccount(AccountDto accountDto) {
        // logic with DB
        return accountDto.getId();
    }

    public AccountDto searchAccountByFilter(AccountByFilterDto filterDto) {
        // logic with DB
        return new AccountDto();
    }

    public AccountDto getAccountById(Long id) {
        // logic with DB
        return new AccountDto();
    }

    public List<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable pageable) {
        // logic with DB
        return List.of(new AccountDto());
    }

    public List<Long> getAllIds() {
        // logic with DB
        return List.of();
    }

    public List<AccountDto> getAccountIds(Long[] ids, Pageable page) {
        // logic with DB
        return List.of();
    }
}
