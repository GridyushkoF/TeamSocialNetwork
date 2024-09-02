package ru.skillbox.authentication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.web.ChangeEmailRequest;
import ru.skillbox.authentication.model.web.ChangePasswordRequest;
import ru.skillbox.authentication.service.UserSecurityDataService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserSecurityController {
    private final UserSecurityDataService userSecurityDataService;
    @PostMapping("/change-password-link")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                               @RequestHeader("id") Long userId) {
        userSecurityDataService.changePassword(changePasswordRequest,userId);
    }
    @PostMapping("/change-email-link")
    public ResponseEntity<SimpleResponse> sendChangeEmailRequest(@RequestBody ChangeEmailRequest changeEmailRequest,
                                                 @RequestHeader("id") Long userId) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(userSecurityDataService.sendEmailChangeRequestToEmail(changeEmailRequest,userId));
    }
    @GetMapping("/change-email/verification/{userEmail}/{changeEmailKey}/confirm")
    public SimpleResponse acceptEmailChanging(@PathVariable String userEmail,
                                              @PathVariable String changeEmailKey) {
        userSecurityDataService.changeEmail(userEmail,changeEmailKey);
        return new SimpleResponse("changed successful");
    }
}
