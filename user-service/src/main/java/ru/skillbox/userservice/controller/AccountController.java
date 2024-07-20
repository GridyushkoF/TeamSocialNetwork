package ru.skillbox.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.skillbox.commondto.account.AccountByFilterDto;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.commondto.account.AccountRecoveryRq;
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
        return ResponseEntity.ok(accountService.getAccountById(Long.parseLong(request.getHeader("id"))));
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateUserAccount(@Valid @RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.updateUserAccount(accountDto, Long.parseLong(request.getHeader("id"))));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount(HttpServletRequest request) {
        return ResponseEntity.ok(accountService.deleteUserAccount(Long.parseLong(request.getHeader("id"))));
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(accountService.blockAccount(true, id));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<String> unblockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(accountService.blockAccount(false, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestParam Pageable page, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getAllAccounts(page, Long.parseLong(request.getHeader("id"))));
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @PostMapping("/searchByFilter")
    public ResponseEntity<AccountDto> searchAccountByFilter(@RequestBody AccountByFilterDto filterDto) {
        return ResponseEntity.ok(accountService.searchAccountByFilter(filterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDto>> searchAccount(@RequestParam boolean isDeleted, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.searchAccount(isDeleted, Long.parseLong(request.getHeader("id"))));
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getAllIds() {
        return ResponseEntity.ok(accountService.getAllIds());
    }

    @GetMapping("/accountIds")
    public ResponseEntity<?> getAccountIds(@RequestParam Long[] ids, @RequestParam Pageable page) {
        return ResponseEntity.ok(accountService.getAccountIds(ids, page));
    }
}
