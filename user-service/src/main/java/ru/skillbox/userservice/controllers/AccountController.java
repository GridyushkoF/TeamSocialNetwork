package ru.skillbox.userservice.controllers;

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
import ru.skillbox.userservice.model.dto.AccountByFilterDto;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.AccountRecoveryRq;
import ru.skillbox.userservice.model.dto.AccountSearchDto;
import ru.skillbox.userservice.services.AccountServices;

import java.security.Principal;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServices accountServices;

    @PutMapping("/recovery")
    public ResponseEntity<String> recoveryUserAccount(@RequestBody AccountRecoveryRq recoveryRq) {
        return ResponseEntity.ok(accountServices.recoveryUserAccount(recoveryRq));
    }

    @GetMapping("/me")
    public ResponseEntity<AccountDto> getUserAccount(Principal principal) {
        return ResponseEntity.ok(accountServices.getUserAccount(principal.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateUserAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountServices.updateUserAccount(accountDto));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount(Principal principal) {
        return ResponseEntity.ok(accountServices.deleteUserAccount(principal.getName()));
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(accountServices.blockAccount(true, id));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<String> unblockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(accountServices.blockAccount(false, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestParam Pageable page) {
        return ResponseEntity.ok(accountServices.getAllAccounts(page));
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountServices.createAccount(accountDto));
    }

    @PostMapping("/searchByFilter")
    public ResponseEntity<AccountDto> searchAccountByFilter(@RequestBody AccountByFilterDto filterDto) {
        return ResponseEntity.ok(accountServices.searchAccountByFilter(filterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountServices.getAccountById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAccount(@RequestParam AccountSearchDto searchDto, @RequestParam Pageable page) {
        return ResponseEntity.ok(accountServices.searchAccount(searchDto, page));
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getAllIds() {
        return ResponseEntity.ok(accountServices.getAllIds());
    }

    @GetMapping("/accountIds")
    public ResponseEntity<?> getAccountIds(@RequestParam Long[] ids, @RequestParam Pageable page) {
        return ResponseEntity.ok(accountServices.getAccountIds(ids, page));
    }
}
