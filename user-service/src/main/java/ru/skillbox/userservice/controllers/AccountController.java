package ru.skillbox.userservice.controllers;

import lombok.RequiredArgsConstructor;
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
import ru.skillbox.userservice.model.dto.Pageable;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    @PutMapping("/recovery")
    public ResponseEntity<String> recoveryUserAccount(@RequestBody AccountRecoveryRq recoveryRq) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/me")
    public ResponseEntity<AccountDto> getUserAccount() {
        return ResponseEntity.ok(new AccountDto());
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateUserAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(new AccountDto());
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount() {
        return ResponseEntity.ok("");
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<String> unblockAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok("");
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestParam Pageable page) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/searchByFilter")
    public ResponseEntity<AccountDto> searchAccountByFilter(@RequestBody AccountByFilterDto filterDto) {
        return ResponseEntity.ok(new AccountDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(new AccountDto());
    }

    @GetMapping("/search")
    public ResponseEntity<AccountDto> searchAccount(@RequestParam AccountSearchDto searchDto, @RequestParam Pageable page) {
        return ResponseEntity.ok(new AccountDto());
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getAllIds() {
        return ResponseEntity.ok("");
    }

    @GetMapping("/accountIds")
    public ResponseEntity<?> getAccountIds(@RequestParam Integer[] ids, @RequestParam Pageable page) {
        return ResponseEntity.ok(new AccountDto());
    }
}
