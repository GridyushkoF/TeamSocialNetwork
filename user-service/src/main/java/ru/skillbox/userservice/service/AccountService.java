package ru.skillbox.userservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.dto.statistics.AgeCountDto;
import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountRecoveryRq;
import ru.skillbox.commonlib.dto.account.AccountSearchDto;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;
import ru.skillbox.commonlib.util.admin.AdminStatisticsRepository;
import ru.skillbox.userservice.exception.AccountAlreadyExistsException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.exception.NotAuthException;
import ru.skillbox.userservice.mapper.V1.UserMapperV1;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;
import ru.skillbox.userservice.util.BeanUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapperV1 userMapper;
    private final AdminStatisticsRepository adminStatisticsRepository;

    public String recoveryUserAccount(AccountRecoveryRq recoveryRequest) {
        return recoveryRequest.getEmail();
    }

    public AccountDto getUserAccountByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new NoSuchAccountException("Can't find Account with email: " + userEmail));
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

            return "blocked";
        } else {
            // logic with DB
            return "unblocked";
        }
    }

    public List<AccountDto> getAllAccounts(Pageable page, Long id) {
        // TODO: когда в репозитории добавим пагинацию - нужно изменить вызов и передавать page
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> userMapper.userToResponse(id, user)).toList();
    }

    @Transactional
    public long createAccount(@Valid AccountDto accountDto) {
        if (userRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with such email already registered!");
        }
        User user = userMapper.requestToUser(accountDto.getId(), accountDto);
        return userRepository.save(user).getId();
    }

    public AccountDto searchAccountByFilter(AccountByFilterDto filterDto) {
        // logic with DB
        return AccountDto.builder().build();
    }

    public AccountDto getAccountById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchAccountException("Can't find Account with id: " + id));
        return userMapper.userToResponse(id, user);
    }

    public List<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable pageable) {
        // logic with DB
        return List.of(AccountDto.builder().build());
    }

    public List<Long> getAllIds() {
        // logic with DB
        return List.of();
    }

    public List<AccountDto> getAccountIds(Long[] ids, Pageable page) {
        // logic with DB
        return List.of();
    }

    public Page<AccountDto> searchAccount(boolean isDeleted, long authUserId) {

        Pageable nextPage = PageRequest.of(0, Integer.MAX_VALUE);

        List<User> users = userRepository.findAllByIsDeleted(nextPage, isDeleted);

        List<AccountDto> pageList = users.stream().map(user -> userMapper.userToResponse(authUserId, user)).toList();

        return new PageImpl<>(pageList, nextPage, users.size());
    }

    public int getRegisteredUsersAmount(PeriodRequestDto periodRequestDto) {
        return userRepository.countByRegDateBetween(
                periodRequestDto.getFirstMonth().toLocalDateTime(),
                periodRequestDto.getLastMonth().toLocalDateTime()
        );
    }
    public UsersStatisticsDto getUsersStatistics(PeriodRequestDto periodRequestDto) {
        int usersAmount = getRegisteredUsersAmount(periodRequestDto);
        List<DateCountPointDto> dateCountStatistics = adminStatisticsRepository.getDateCountStatistics(
                "regDate",
                "MONTH",
                "User",
                periodRequestDto.getFirstMonth().toLocalDateTime(),
                periodRequestDto.getLastMonth().toLocalDateTime());
        List<AgeCountDto> ageDate = userRepository.findAgeCountStatistics();
        return new UsersStatisticsDto(usersAmount, ageDate, dateCountStatistics);
    }

}
