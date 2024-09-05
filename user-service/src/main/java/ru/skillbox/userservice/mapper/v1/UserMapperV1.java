package ru.skillbox.userservice.mapper.v1;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.userservice.model.dto.FriendShortDto;
import ru.skillbox.userservice.model.entity.User;

@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperV1 {
    @Mapping(target = "regDate", ignore = true)
    @Mapping(target = "lastOnlineTime", ignore = true)
    @Mapping(target = "isOnline", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(source = "authUserId", target = "id")
    User requestToUser(Long authUserId, AccountDto request);

    AccountDto userToResponse(Long authUserId, User user);
}
