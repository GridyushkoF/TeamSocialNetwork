package ru.skillbox.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.userservice.model.entity.User;

import java.util.List;

/**
 * Интерфейс маппера для преобразования между DTO (Data Transfer Objects) и сущностями,
 * связанными с операциями пользовательского сервиса.
 *
 * <p> Этот маппер использует MapStruct с интеграцией Spring для автоматической генерации
 * реализаций маппингов на основе сигнатур методов, определенных здесь. Он настроен на
 * игнорирование немаппированных исходных свойств для упрощения маппингов и уменьшения
 * объема лишнего кода.
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserServiceMapper {

    /**
     * Преобразует объект AccountDto в сущность User.
     *
     * @param accountDto DTO с данными аккаунта
     * @return сущность User, соответствующая переданному DTO
     */
    User map(AccountDto accountDto);

    /**
     * Преобразует сущность User в DTO AccountDto.
     *
     * @param user сущность User
     * @return DTO AccountDto, содержащий данные сущности User
     */
    AccountDto map(User user);

    /**
     * Преобразует список сущностей User в список DTO AccountDto.
     *
     * @param users список сущностей User
     * @return список DTO AccountDto, содержащий данные каждой сущности User
     */
    List<AccountDto> map(List<User> users);
}
