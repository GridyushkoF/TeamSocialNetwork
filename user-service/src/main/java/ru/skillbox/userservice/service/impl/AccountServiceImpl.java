package ru.skillbox.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.exceptions.AccountAlreadyExistsException;
import ru.skillbox.userservice.exceptions.NoSuchAccountException;
import ru.skillbox.userservice.exceptions.NotAuthException;
import ru.skillbox.userservice.mapper.UserServiceMapper;
import ru.skillbox.userservice.model.dto.AccountByFilterDto;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.AccountRecoveryRq;
import ru.skillbox.userservice.model.dto.AccountSearchDto;
import ru.skillbox.userservice.repository.UserRepository;
import ru.skillbox.userservice.service.AccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final UserServiceMapper userServiceMapper;

    @Override
    public String recoveryUserAccount(AccountRecoveryRq recoveryRequest) {
        // logic with DB
        return recoveryRequest.getEmail();
    }

    @Override
    public AccountDto getUserAccount(String userEmail) {
        var user = userRepository.findUserByEmail(userEmail);
        return userServiceMapper.map(user.orElseThrow(() ->
                new NoSuchAccountException("Can't find Account with email: " + userEmail)));
    }

    @Override
    public AccountDto updateUserAccount(AccountDto accountDto) {
        // logic with DB
        return new AccountDto();
    }

    @Override
    public String deleteUserAccount(String userEmail) {
        if (userEmail == null) {
            throw new NotAuthException("No auth user found!");
        }
        // logic with DB
        return userEmail;
    }

    @Override
    public String blockAccount(boolean block, long id) {
        if (block) {
            // logic with DB
            return "blocked";
        } else {
            // logic with DB
            return "unblocked";
        }
    }

    @Override
    public List<AccountDto> getAllAccounts(Pageable page) {
        // TODO: когда в репозитории добавим пагинацию - нужно изменить вызов и передавать page
        var users = userRepository.findAll();
        return userServiceMapper.map(users);
    }

    @Override
    public long createAccount(AccountDto accountDto) {
        if (userRepository.findUserByEmail(accountDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with such email already registered!");
        }
        return userRepository.save(userServiceMapper.map(accountDto)).getId();
    }

    @Override
    public AccountDto searchAccountByFilter(AccountByFilterDto filterDto) {
        // logic with DB
        return new AccountDto();
    }

    @Override
    public AccountDto getAccountById(Long id) {
        var user = userRepository.findById(id);
        return userServiceMapper.map(user.orElseThrow(() ->
                new NoSuchAccountException("Can't find Account with id: " + id)));
    }

    @Override
    public List<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable pageable) {
        // logic with DB
        return List.of(new AccountDto());
    }

    @Override
    public List<Long> getAllIds() {
        // logic with DB
        return List.of();
    }

    @Override
    public List<AccountDto> getAccountIds(Long[] ids, Pageable page) {
        // logic with DB
        return List.of();
    }
}
