package ru.skillbox.userservice.service;

import org.springframework.data.domain.Pageable;
import ru.skillbox.userservice.model.dto.AccountByFilterDto;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.AccountRecoveryRq;
import ru.skillbox.userservice.model.dto.AccountSearchDto;

import java.util.List;

/**
 * Сервис аккаунтов
 */
public interface AccountService {

    //TODO: добавить javadoc для методов
    /**
     *
     * @param recoveryRequest
     * @return
     */
    String recoveryUserAccount(AccountRecoveryRq recoveryRequest);

    AccountDto getUserAccount(String userEmail);

    AccountDto updateUserAccount(AccountDto accountDto);

    String deleteUserAccount(String userEmail);

    String blockAccount(boolean block, long id);

    List<AccountDto> getAllAccounts(Pageable page);

    long createAccount(AccountDto accountDto);

    AccountDto searchAccountByFilter(AccountByFilterDto filterDto);

    AccountDto getAccountById(Long id);

    List<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable pageable);

    List<Long> getAllIds();

    List<AccountDto> getAccountIds(Long[] ids, Pageable page);
}
