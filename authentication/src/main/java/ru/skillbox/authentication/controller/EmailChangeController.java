package ru.skillbox.authentication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.service.UserSecurityDataService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/change-email/verification")
public class EmailChangeController {
    private final UserSecurityDataService userSecurityDataService;
    @GetMapping("/{userEmail}/{changeEmailKey}/confirm")
    public SimpleResponse acceptEmailChanging(@PathVariable String userEmail,
                                              @PathVariable String changeEmailKey) {
        userSecurityDataService.changeEmail(userEmail,changeEmailKey);
        return new SimpleResponse("changed successful");
    }
}
