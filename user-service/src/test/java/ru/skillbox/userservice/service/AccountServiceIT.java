package ru.skillbox.userservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountRecoveryRequest;
import ru.skillbox.commonlib.dto.account.AccountSearchDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;
import ru.skillbox.userservice.exception.AccountAlreadyExistsException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.exception.NotAuthException;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class AccountServiceIT {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("test recovery of user account")
    void testRecoveryUserAccount_correct_success() {
        AccountRecoveryRequest recoveryRequest = new AccountRecoveryRequest();
        recoveryRequest.setEmail("test@example.com");
        String email = accountService.recoveryUserAccount(recoveryRequest);
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("test update of user account")
    void testUpdateUserAccount_correct_success() {
        User user = createTestUser();
        AccountDto accountDto = AccountDto.builder().build();
        accountDto.setId(user.getId());
        accountDto.setAbout("updated");
        AccountDto updatedAccount = accountService.updateUserAccount(accountDto, user.getId());
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found"));
        assertEquals("updated", updatedUser.getAbout());
        assertEquals("updated", updatedAccount.getAbout());
    }

    @Test
    @DisplayName("test update of user account, not auth, exception")
    void testUpdateUserAccount_notAuthUser_throwException() {
        User user = createTestUser();
        AccountDto accountDto = AccountDto.builder().build();
        accountDto.setId(user.getId());
        accountDto.setAbout("updated");
        assertThrows(NotAuthException.class, () -> accountService.updateUserAccount(accountDto, 2L));
    }

    @Test
    @DisplayName("test delete of user account, correct, success")
    void testDeleteUserAccount_correct_success() {
        User user = createTestUser();
        String result = accountService.deleteUserAccount(user.getId());
        User deletedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Deleted user not found"));
        assertTrue(deletedUser.isDeleted());
        assertTrue(result.contains("deleted"));
    }

    @Test
    @DisplayName("test delete of user account, no auth, exception")
    void testDeleteUserAccount_noUserId_throwException() {
        assertThrows(NotAuthException.class, () -> accountService.deleteUserAccount(null));
    }

    @Test
    @DisplayName("test block of user account, correct, success")
    void testBlockAccount_correct_success() {
        User user = createTestUser();
        String result = accountService.blockAccount(true, user.getId());

        User blockedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found"));
        assertTrue(blockedUser.isBlocked());
        assertTrue(result.contains("Block or unblock ready!"));
    }

    @Test
    @DisplayName("test get all accounts, correct, success")
    void testGetAllAccounts_correct_success() {
        createTestUser();
        Pageable page = PageRequest.of(0, 10);
        Page<AccountDto> accountsPage = accountService.getAllAccounts(page, 1L);
        assertNotNull(accountsPage);
        assertEquals(1, accountsPage.getTotalElements());
    }

    @Test
    @DisplayName("test create account, correct, success")
    void testCreateAccount_correct_success() {
        AccountDto accountDto = AccountDto.builder().build();
        accountDto.setEmail("test@example.com");
        accountDto.setFirstName("John");
        long id = accountService.createAccount(accountDto, 1L);
        User createdUser = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Created user not found"));
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("John", createdUser.getFirstName());
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("test create account, email already exists, exception")
    void testCreateAccount_emailAlreadyExists_throwException() {
        AccountDto accountDto = AccountDto.builder().build();
        accountDto.setEmail("test@example.com");
        createTestUser();
        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(accountDto, 1L));
    }

    @Test
    @DisplayName("test create account, correct, success")
    void testSearchAccountByFilter_correct_success() {
        createTestUser();
        AccountSearchDto accountSearchDto = AccountSearchDto.builder()
                .firstName("John")
                .build();
        AccountByFilterDto filterDto = AccountByFilterDto.builder()
                .accountSearchDto(accountSearchDto)
                .pageSize(10)
                .pageNumber(0)
                .build();
        List<AccountDto> accounts = accountService.searchAccountByFilter(filterDto, 1L);
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
    }

    @Test
    @DisplayName("test get account by id, correct, success")
    void testGetAccountById_correct_success() {
        User user = createTestUser();
        AccountDto account = accountService.getAccountById(user.getId(), 1L);
        assertNotNull(account);
        assertEquals(user.getId(), account.getId());
    }

    @Test
    @DisplayName("test get account by id, no such account, exception")
    void testGetAccountById_noSuchAccount_throwException() {
        assertThrows(NoSuchAccountException.class, () ->
                accountService.getAccountById(1L, 1L));
    }

    @Test
    @DisplayName("test get users statistics by , correct, success")
    void testGetUsersStatistics_correct_success() {
        createTestUser();
        PeriodRequestDto periodRequestDto = PeriodRequestDto.builder()
                .firstMonth(ZonedDateTime.now().with(TemporalAdjusters.firstDayOfMonth()))
                .lastMonth(ZonedDateTime.now().with(TemporalAdjusters.lastDayOfMonth()))
                .build();
        UsersStatisticsDto statistics = accountService.getUsersStatistics(periodRequestDto);

        assertNotNull(statistics);
        assertEquals(1, statistics.getCount());
    }

    private User createTestUser() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .about("About info")
                .regDate(ZonedDateTime.now().minusWeeks(1))
                .birthDate(ZonedDateTime.now().minusYears(25))
                .build();
        return userRepository.save(user);
    }
}