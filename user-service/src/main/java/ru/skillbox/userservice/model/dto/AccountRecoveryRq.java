package ru.skillbox.userservice.model.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountRecoveryRq {
    @Email(message = "Необходимо заполнить в формате Email: someuser@abc.com")
    String email;

    @Length(min = 8, message = "Минимальная длина пароля 8 символов!")
    String password;
}
