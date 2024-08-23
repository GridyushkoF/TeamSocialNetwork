package ru.skillbox.userservice.controller;

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
    public ResponseEntity<AccountDto> getUserAccount(HttpServletRequest request) {
        Long myId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(accountService.getAccountById(myId, myId));
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateUserAccount(@Valid @RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.updateUserAccount(accountDto, Long.parseLong(request.getHeader("id"))));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount(HttpServletRequest request) {
        return ResponseEntity.ok(accountService.deleteUserAccount(Long.parseLong(request.getHeader("id"))));
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
    public ResponseEntity<Long> createAccount(@RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountDto, Long.parseLong(request.getHeader("id"))));
    }

    @PostMapping("/searchByFilter")
    public ResponseEntity<List<AccountDto>> searchAccountByFilter(@RequestBody AccountByFilterDto filterDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.searchAccountByFilter(filterDto, Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAccountById(id, Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountDto>> searchAccount(
            @RequestParam boolean isDeleted,
          HttpServletRequest request) {
        return ResponseEntity.ok(accountService
                .searchAccount(isDeleted,
                        Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getAllIds() {
        return ResponseEntity.ok(accountService.getAllIds());
    }

    @GetMapping("/accountIds")
    public ResponseEntity<List<AccountDto>> getAccountIds(@RequestParam Long[] ids, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAccountIds(ids, Long.parseLong(request.getHeader("id"))));
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
