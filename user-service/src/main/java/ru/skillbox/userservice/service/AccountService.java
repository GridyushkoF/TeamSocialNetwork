package ru.skillbox.userservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commondto.account.AccountByFilterDto;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.commondto.account.AccountRecoveryRq;
import ru.skillbox.commondto.account.AccountSearchDto;
import ru.skillbox.userservice.controller.AccountPredicate;
import ru.skillbox.userservice.exception.AccountAlreadyExistsException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.exception.NotAuthException;
import ru.skillbox.userservice.mapper.V1.UserMapperV1;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;
import ru.skillbox.userservice.util.BeanUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapperV1 userMapper;

    public String recoveryUserAccount(AccountRecoveryRq recoveryRequest) {
        return recoveryRequest.getEmail();
    }

    public AccountDto getUserAccount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchAccountException("Can't find Account with email: " + userEmail));
        return userMapper.userToResponse(user.getId(), user);
    }

    @Transactional
    public AccountDto updateUserAccount(AccountDto accountDto, Long id) {
        if (accountDto.getId() != null && !accountDto.getId().equals(id))
            throw new NotAuthException("Can't update Account with id:" + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchAccountException("Can't find Account with id:" + id));
        AccountDto existedAccount = userMapper.userToResponse(id, user);
        BeanUtil.copyNonNullProperties(accountDto, existedAccount);

        return userMapper.userToResponse(id, userRepository.save(userMapper.requestToUser(id, existedAccount)));
    }

    @Transactional
    public String deleteUserAccount(Long userId) {
        if (userId == null) {
            throw new NotAuthException("No auth user found!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchAccountException("Can't delete Account with id:" + userId));
        user.setDeleted(true);
        userRepository.save(user);

        return "Account with id: " + userId + " deleted";
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

    public Page<AccountDto> getAllAccounts(Pageable page, Long id) {
        List<User> users = userRepository.findAllByIsDeleted(page, false);
        List<AccountDto> pageList = users.stream().map(user -> userMapper.userToResponse(id, user)).toList();
        return new PageImpl<>(pageList, page, users.size());
    }

    @Transactional
    public long createAccount(@Valid AccountDto accountDto) {
        if (userRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with such email already registered!");
        }
        User user = userMapper.requestToUser(accountDto.getId(), accountDto);
        return userRepository.save(user).getId();
    }

    public List<AccountDto> searchAccountByFilter(AccountByFilterDto filterDto) {
        Stream<User> users = userRepository.findAll().stream();
        AccountSearchDto params = filterDto.getAccountSearchDto();
        users = users
                .filter(AccountPredicate.checkIds(params.getIds()))
                .filter(AccountPredicate.checkFirstName(params.getFirstName()))
                .filter(AccountPredicate.checkFirstName(params.getLastName()))
                .filter(AccountPredicate.birthdayBetween(params.getBirthDateFrom(), params.getBirthDateTo()))
                .filter(AccountPredicate.checkAge(params.getAgeFrom(), params.getAgeTo()));
        return users.map(user -> userMapper.userToResponse(user.getId(), user)).toList();
    }

    public AccountDto getAccountById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchAccountException("Can't find Account with id: " + id));
        return userMapper.userToResponse(id, user);
    }

    public List<Long> getAllIds() {
        return userRepository.findAll().stream().map(User::getId).toList();
    }

    public List<AccountDto> getAccountIds(Long[] ids, Pageable page) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> Arrays.asList(ids).contains(user.getId()))
                .map(user -> userMapper.userToResponse(user.getId(), user))
                .toList();
    }

    public Page<AccountDto> searchAccount(boolean isDeleted, long authUserId) {
        Pageable nextPage = PageRequest.of(0, Integer.MAX_VALUE);
        List<User> users = userRepository.findAllByIsDeleted(nextPage, isDeleted);
        List<AccountDto> pageList = users.stream().map(user -> userMapper.userToResponse(authUserId, user)).toList();
        return new PageImpl<>(pageList, nextPage, users.size());
    }
}
