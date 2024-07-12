package ru.skillbox.userservice.mapper.V1;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.userservice.model.entity.User;

@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperV1 {
    User requestToUser(Long authUserId, AccountDto request);

    AccountDto userToResponse(Long authUserId, User user);

}
