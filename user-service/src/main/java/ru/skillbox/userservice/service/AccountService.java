package ru.skillbox.userservice.service;


import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountRecoveryRequest;
import ru.skillbox.commonlib.dto.account.AccountSearchDto;
import ru.skillbox.commonlib.dto.statistics.AgeCountDto;
import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;
import ru.skillbox.commonlib.event.audit.ActionType;
import ru.skillbox.commonlib.util.ColumnsUtil;
import ru.skillbox.commonlib.util.admin.AdminStatisticsRepository;
import ru.skillbox.userservice.exception.AccountAlreadyExistsException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.exception.NotAuthException;
import ru.skillbox.userservice.mapper.v1.UserMapperV1;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.processor.AuditProcessor;
import ru.skillbox.userservice.repository.UserRepository;
import ru.skillbox.userservice.service.specifiaction_api.AccountPredicate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapperV1 userMapper;
    private final AdminStatisticsRepository adminStatisticsRepository;
    private final MeterRegistry meterRegistry;
    private final AuditProcessor auditProcessor;

    @Scheduled(fixedRate = 5000)
    public void updateBlockedUsersAmountMetrics() {
        Long blockedUsersAmount = userRepository.countByIsBlocked(true);
        log.info("Blocked users amount: " + blockedUsersAmount);
        meterRegistry.gauge("users.blocked", blockedUsersAmount);
    }

    public String recoveryUserAccount(AccountRecoveryRequest recoveryRequest) {
        return recoveryRequest.getEmail();
    }

    @Transactional
    public AccountDto updateUserAccount(AccountDto accountDto, Long authUserId) {
        if (accountDto.getId() != null && !accountDto.getId().equals(authUserId))
            throw new NotAuthException("Can't update Account with id:" + authUserId);
        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new NoSuchAccountException("Can't find Account with id:" + authUserId));
        AccountDto existedAccount = userMapper.userToResponse(authUserId, user);

        ColumnsUtil.copyNonNullProperties(accountDto,existedAccount);
        User newUser = userMapper.requestToUser(authUserId, existedAccount);
        newUser.setFriendsFrom(user.getFriendsFrom());
        newUser.setFriendsTo(user.getFriendsTo());

        auditProcessor.process(newUser, ActionType.UPDATE, authUserId);

        return userMapper.userToResponse(authUserId, userRepository.save(newUser));
    }

    @Transactional
    public String deleteUserAccount(Long userId) {
        if (userId == null) {
            throw new NotAuthException("No auth user found!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchAccountException("Can't delete Account with id:" + userId));
        user.setDeleted(true);
        userRepository.save(user);

        auditProcessor.process(user, ActionType.DELETE, userId);

        return "Account with id: " + userId + " deleted";
    }

    @Transactional
    public String blockAccount(boolean block, long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setBlocked(block);
            userRepository.save(user);
            auditProcessor.process(user, ActionType.UPDATE, id);
        });
        return "Block or unblock ready!";
    }

    public Page<AccountDto> getAllAccounts(Pageable page, Long authUserId) {
        List<User> users = userRepository.findAllByIsDeletedAndIdNot(page, false, authUserId);
        List<AccountDto> pageList = users.stream().map(user -> userMapper.userToResponse(authUserId, user)).toList();
        return new PageImpl<>(pageList, page, users.size());
    }

    @Transactional
    public long createAccount(@Valid AccountDto accountDto, Long authUserId) {
        if (userRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with such email already registered!");
        }
        User user = userMapper.requestToUser(authUserId, accountDto);

        auditProcessor.process(user, ActionType.CREATE, authUserId);

        return userRepository.save(user).getId();
    }

    public List<AccountDto> searchAccountByFilter(AccountByFilterDto filterDto, Long authUserId) {
        Stream<User> users = userRepository.findAll().stream();
        AccountSearchDto params = filterDto.getAccountSearchDto();
        users = users
                .filter(AccountPredicate.checkIsDeleted(params.isDeleted()))
                .filter(AccountPredicate.checkIds(params.getIds()))
                .filter(AccountPredicate.checkFirstName(params.getFirstName()))
                .filter(AccountPredicate.checkLastName(params.getLastName()))
                .filter(AccountPredicate.checkCountry(params.getCountry()))
                .filter(AccountPredicate.checkCity(params.getCity()))
                .filter(AccountPredicate.birthdayBetween(params.getBirthDateFrom(), params.getBirthDateTo()))
                .filter(AccountPredicate.checkAge(params.getAgeFrom(), params.getAgeTo()));
        return users.map(user -> userMapper.userToResponse(authUserId, user)).toList();
    }

    public AccountDto getAccountById(Long id, Long authUserId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchAccountException("Can't find Account with id: " + id));
        return userMapper.userToResponse(authUserId, user);
    }

    public List<Long> getAllIds() {
        return userRepository.findAllIds();
    }

    public List<AccountDto> getAccountIds(Long[] ids, Long authUserId) {
        List<User> users = userRepository.findAllByIdIn(Arrays.asList(ids));
        return users.stream()
                .map(user -> userMapper.userToResponse(authUserId, user))
                .toList();
    }

    public UsersStatisticsDto getUsersStatistics(PeriodRequestDto periodRequestDto) {
        long usersAmount = userRepository.count();
        List<DateCountPointDto> dateCountStatistics = adminStatisticsRepository.getDateCountStatistics(
                "regDate",
                "MONTH",
                "User",
                periodRequestDto.getFirstMonth(),
                periodRequestDto.getLastMonth());
        List<AgeCountDto> ageDate = userRepository.findAgeCountStatistics();
        return new UsersStatisticsDto(usersAmount, ageDate, dateCountStatistics);
    }

    public Page<AccountDto> searchAccountByFilterParams(
            boolean isDeleted, Integer size, List<Long> ids,
            String firstName, String lastName, Integer ageFrom, Integer ageTo, String country, String city, long id) {
        AccountSearchDto params = AccountSearchDto.builder()
                .ids(ids)
                .isDeleted(isDeleted)
                .firstName(firstName)
                .lastName(lastName)
                .ageFrom(ageFrom)
                .ageTo(ageTo)
                .city(city)
                .country(country)
                .build();
        AccountByFilterDto filterDto = AccountByFilterDto.builder()
                .pageNumber(0)
                .pageSize(size)
                .accountSearchDto(params).build();
        Pageable nextPage = PageRequest.of(0, size);
        List<AccountDto> pageList = searchAccountByFilter(filterDto, id);
        return new PageImpl<>(pageList, nextPage, pageList.size());
    }
}

