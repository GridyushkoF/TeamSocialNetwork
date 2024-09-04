package ru.skillbox.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@Tag(name = "Account Controller", description = "Account API")
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/recovery")
    @Operation(summary = "Recovery user account")
    public ResponseEntity<String> recoveryUserAccount(
            @Valid
            @RequestBody AccountRecoveryRequest recoveryRequest) {
        return ResponseEntity.ok(accountService
                .recoveryUserAccount(recoveryRequest));
    }

    @GetMapping("/me")
    @Operation(summary = "Get user account")
    public ResponseEntity<AccountDto> getUserAccount(HttpServletRequest request) {
        Long myId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(accountService.getAccountById(myId, myId));
    }

    @PutMapping("/me")
    @Operation(summary = "Update user account")
    public ResponseEntity<AccountDto> updateUserAccount(@Valid @RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.updateUserAccount(accountDto, Long.parseLong(request.getHeader("id"))));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete user account")
    public ResponseEntity<String> deleteUserAccount(HttpServletRequest request) {
        return ResponseEntity.ok(accountService.deleteUserAccount(Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
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
    @Operation(summary = "Create user account")
    public ResponseEntity<Long> createAccount(@RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountDto, Long.parseLong(request.getHeader("id"))));
    }

    @PostMapping("/searchByFilter")
    @Operation(summary = "Search account by filter")
    public ResponseEntity<List<AccountDto>> searchAccountByFilter(@RequestBody AccountByFilterDto filterDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.searchAccountByFilter(filterDto, Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user account by id")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAccountById(id, Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/search")
    @Operation(summary = "Search user account")
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
          HttpServletRequest request) {
        return ResponseEntity.ok(accountService
                .searchAccountByFilterParams(isDeleted, size, ids, firstName, lastName, ageFrom, ageTo, country, city,
                        Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/ids")
    @Operation(summary = "Get all IDs")
    public ResponseEntity<List<Long>> getAllIds() {
        return ResponseEntity.ok(accountService.getAllIds());
    }

    @GetMapping("/accountIds")
    @Operation(summary = "Get account IDs")
    public ResponseEntity<List<AccountDto>> getAccountIds(@RequestParam Long[] ids, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAccountIds(ids, Long.parseLong(request.getHeader("id"))));
    }
    //----------------------------ADMIN-ACCESS---------------------------
    @PostMapping("/statistic")
    @Operation(summary = "Get users statistics")
    public ResponseEntity<UsersStatisticsDto> getUsersStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.getUsersStatistics(periodRequestDto));
    }

    @PutMapping("/block/{id}")
    @Operation(summary = "Block user account by ID")
    public ResponseEntity<String> blockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(true, id));
    }

    @DeleteMapping("/block/{id}")
    @Operation(summary = "Unblock user account by ID")
    public ResponseEntity<String> unblockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(false, id));
    }
}
