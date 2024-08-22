package ru.skillbox.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.account.AccountByFilterDto;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.commonlib.dto.account.AccountRecoveryRq;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;
import ru.skillbox.commonlib.util.admin.AdminAccessUtil;
import ru.skillbox.userservice.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/recovery")
    public ResponseEntity<String> recoveryUserAccount(@Valid @RequestBody AccountRecoveryRq recoveryRq) {
        return ResponseEntity.ok(accountService.recoveryUserAccount(recoveryRq));
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
    public ResponseEntity<Page<AccountDto>> getAllAccounts(@RequestParam Pageable page, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAllAccounts(page, Long.parseLong(request.getHeader("id"))));
    }

    @PostMapping
    public ResponseEntity<Long> createAccount(@RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto, Long.parseLong(request.getHeader("id"))));
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
    public ResponseEntity<Page<AccountDto>> searchAccount(@RequestParam boolean isDeleted, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.searchAccount(isDeleted, Long.parseLong(request.getHeader("id"))));
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

    @PutMapping("/admin-api/block/{id}")
    public ResponseEntity<String> blockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(true, id));
    }

    @DeleteMapping("/admin-api/block/{id}")
    public ResponseEntity<String> unblockAccountById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(accountService.blockAccount(false, id));
    }
}
