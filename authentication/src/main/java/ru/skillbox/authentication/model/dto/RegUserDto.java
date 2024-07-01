package ru.skillbox.authentication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User registration DTO")
public class RegUserDto {

    @Schema(description = "User email", example = "test@mail.ru")
    @NotNull(message = "Поле email не может быть пустым")
    private String email;

    @NotNull(message = "Поле password не может быть пустым")
    @Schema(description = "User password", example = "12345")
    private String password1;

    @NotNull(message = "Поле password не может быть пустым")
    @Schema(description = "User password repeat", example = "12345")
    private String password2;

    @NotNull(message = "Поле firstname не может быть пустым")
    @Schema(description = "User first name", example = "Ivan")
    private String firstName;

    @NotNull(message = "Поле lastname не может быть пустым")
    @Schema(description = "User last name", example = "Ivanov")
    private String lastName;

    @NotNull(message = "Поле password не может быть пустым")
    @Schema(description = "Code", example = "????")
    private String code;

    @NotNull(message = "Поле token не может быть пустым")
    @Schema(description = "Token", example = "????")
    private String token;
}
