package ru.skillbox.userservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.account.AccountSearchDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountRecoveryRequest;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;
import ru.skillbox.commonlib.util.SortCreatorUtil;
import ru.skillbox.commonlib.util.admin.AdminAccessUtil;
import ru.skillbox.userservice.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/recovery")
    public ResponseEntity<String> recoveryUserAccount(
            @Valid
            @RequestBody AccountRecoveryRequest recoveryRequest) {
        return ResponseEntity.ok(accountService
                .recoveryUserAccount(recoveryRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<AccountDto> getUserAccount(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.getAccountById(currentAuthUserId, currentAuthUserId));
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateUserAccount(
            @Valid @RequestBody AccountDto accountDto,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.updateUserAccount(accountDto, currentAuthUserId));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount(@RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.deleteUserAccount(currentAuthUserId));
    }

    @GetMapping
    public ResponseEntity<Page<AccountDto>> getAllAccounts(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort,
            @RequestHeader Long id) {
        return ResponseEntity.ok(accountService
                .getAllAccounts(PageRequest.of(page,size,
                        SortCreatorUtil.createSort(sort)), id));
    }

    @PostMapping
    public ResponseEntity<Long> createAccount(
            @RequestBody AccountDto accountDto,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountDto, currentAuthUserId));
    }

    @PostMapping("/searchByFilter")
    public ResponseEntity<List<AccountDto>> searchAccountByFilter(
            @RequestBody AccountByFilterDto filterDto,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.searchAccountByFilter(filterDto, currentAuthUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable Long id,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.getAccountById(id, currentAuthUserId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountDto>> searchAccount(
            @RequestParam boolean isDeleted,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer ageFrom,
            @RequestParam(required = false) Integer ageTo,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestHeader("id") Long currentAuthUserId) {

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

        return ResponseEntity.ok(accountService.searchAccountByFilterParams(size, params, currentAuthUserId));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getAllIds() {
        return ResponseEntity.ok(accountService.getAllIds());
    }

    @GetMapping("/accountIds")
    public ResponseEntity<List<AccountDto>> getAccountIds(
            @RequestParam Long[] ids,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(accountService.getAccountIds(ids, currentAuthUserId));
    }
    //----------------------------ADMIN-ACCESS---------------------------
    @PostMapping("/statistic")
    public ResponseEntity<UsersStatisticsDto> getUsersStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.getUsersStatistics(periodRequestDto));
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(true, id));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<String> unblockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(false, id));
    }
}
