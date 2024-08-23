package ru.skillbox.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountSearchDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.userservice.mapper.v1.UserMapperV1;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapperV1 userMapperV1;

    @Autowired
    private ObjectMapper objectMapper;
    @Value("${custom.rest.basePath}")
    private String apiPrefix;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("test get user account, exists, success")
    void testGetUserAccount_accountExists_successGet() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account/me")
                        .header("id", accountDto.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountDto.getId()));
    }

    @Test
    @DisplayName("test get user account, not exists, bad request")
    void testGetUserAccount_accountNotExists_badRequest() throws Exception {
        mockMvc.perform(get(apiPrefix + "/account/me")
                        .header("id", 45246246L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("test update user account, correct, success")
    void testUpdateUserAccount_correct_successUpdated() throws Exception {
        AccountDto user = createTestUserInDb();
        user.setAbout("updated");
        mockMvc.perform(put(apiPrefix + "/account/me")
                        .header("id", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.about")
                        .value("updated"));
    }

    @Test
    @DisplayName("test update user account, try to update account of not auth user, auth error")
    void testUpdateUserAccount_tryToUpdateNotYourAccount_unauthorized() throws Exception {
        AccountDto user = createTestUserInDb();
        user.setAbout("updated");
        mockMvc.perform(put(apiPrefix + "/account/me")
                        .header("id", 13452463456L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("test delete user account, correct, success")
    void testDeleteUserAccount_correct_successDeleted() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(delete(apiPrefix + "/account/me")
                        .header("id", accountDto.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("test delete user account, not exists, bad request")
    void testDeleteUserAccount_accountNotExists_badRequest() throws Exception {
        createTestUserInDb();
        mockMvc.perform(delete(apiPrefix + "/account/me")
                        .header("id", 245245L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("test create user account, correct, success")
    void testCreateAccount_correct_successCreated() throws Exception {
        String accountDtoJson = "{\"firstName\": \"newuser\", \"email\": \"new@example.com\"}";

        mockMvc.perform(post(apiPrefix + "/account")
                        .header("id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountDtoJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber());
    }

    @Test
    @DisplayName("test get all accounts, correct, success")
    void testGetAllAccounts_correct_successGet() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account")
                        .header("id", accountDto.getId())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,ASC"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("test get account by id, correct, success")
    void testGetAccountById_correct_successGet() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account/{id}", accountDto.getId())
                        .header("id", accountDto.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(accountDto.getId()));
    }

    @Test
    @DisplayName("test get account by id, account not exists, bad request")
    void testGetAccountById_accountNotExists_badRequest() throws Exception {
        mockMvc.perform(get(apiPrefix + "/account/{id}", 145145145L)
                        .header("id", 1L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("test search account by filter, correct, success")
    void testSearchAccountByFilter_correct_expectedResultsFound() throws Exception {
        createTestUserInDb();
        AccountSearchDto accountSearchDto = AccountSearchDto.builder()
                .firstName("testUser")
                .build();
        AccountByFilterDto filterDto = AccountByFilterDto.builder()
                .accountSearchDto(accountSearchDto)
                .pageSize(10)
                .pageNumber(0)
                .build();
        mockMvc.perform(post(apiPrefix + "/account/searchByFilter")
                        .header("id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$",
                        Matchers.hasSize(1)));
    }
    @Test
    @DisplayName("test search account by filter, correct, results not found")
    void testSearchAccountByFilter_correct_expectedResultsNotFound() throws Exception {
        createTestUserInDb();
        AccountSearchDto accountSearchDto = AccountSearchDto.builder()
                .firstName("other")
                .build();
        AccountByFilterDto filterDto = AccountByFilterDto.builder()
                .accountSearchDto(accountSearchDto)
                .pageSize(10)
                .pageNumber(0)
                .build();
        mockMvc.perform(post(apiPrefix + "/account/searchByFilter")
                        .header("id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$",
                        Matchers.hasSize(0)));
    }
    @Test
    @DisplayName("test search account, correct, success")
    void testSearchAccount_correct_accountFound() throws Exception {
        createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account/search")
                        .header("id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("isDeleted", "false")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        Matchers.hasSize(1)));

    }

    @Test
    @DisplayName("test get all ids, correct, success")
    void testGetAllIds_correct_successFound() throws Exception {
        createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account/ids")
                        .header("id", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",
                        Matchers.hasSize(1)));

    }

    @Test
    @DisplayName("test get all accounts by ids list, correct, success")
    void testGetAccountsIds_correct_successGet() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(get(apiPrefix + "/account/accountIds")
                        .header("id", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", accountDto.getId().toString())
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",
                        Matchers.hasSize(1)));

    }

    @Test
    @DisplayName("test get statistics, correct, success")
    void testGetUsersStatistics_correct_successGet() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        PeriodRequestDto periodRequestDto = PeriodRequestDto.builder()
                .firstMonth(ZonedDateTime.now()
                        .with(TemporalAdjusters.firstDayOfMonth()))
                .lastMonth(ZonedDateTime.now()
                        .with(TemporalAdjusters.lastDayOfMonth()))
                .build();

        mockMvc.perform(post(apiPrefix + "/account/statistic")
                        .header("id", accountDto.getId())
                        .header("authorities", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(periodRequestDto))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count",
                        Matchers.equalTo(1)));

    }

    @Test
    @DisplayName("test block user, correct, success")
    void testBlockAccountById_correct_successBlock() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(put(apiPrefix + "/account/block/" + accountDto.getId())
                        .header("id", 100)
                        .header("authorities", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    @DisplayName("test block user, user not admin, unauthorized")
    void testBlockAccountById_notAdmin_unauthorized() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(put(apiPrefix + "/account/block/" + accountDto.getId())
                        .header("id", 100)
                        .header("authorities", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @DisplayName("test unblock user, correct, success")
    void testUnblockAccountById_correct_successUnblock() throws Exception {
        AccountDto accountDto = createTestUserInDb();
        mockMvc.perform(delete(apiPrefix + "/account/block/" + accountDto.getId())
                        .header("id", 100)
                        .header("authorities", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    //-------------UTIL-METHODS---------------
    private AccountDto createTestUserInDb() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("testUser")
                .lastName("testUser")
                .password("123")
                .about("someAboutInfo")
                .regDate(ZonedDateTime.now().minusWeeks(7))
                .birthDate(ZonedDateTime.now().minusYears(18))
                .build();
        userRepository.save(user);
        return userMapperV1.userToResponse(user.getId(), user);
    }
}