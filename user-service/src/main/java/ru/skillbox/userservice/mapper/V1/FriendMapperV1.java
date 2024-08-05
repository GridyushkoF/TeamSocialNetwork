package ru.skillbox.userservice.mapper.V1;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.userservice.model.dto.FriendDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendMapperV1 {

    @Mapping(source = "id", target = "friendId")
    FriendDto accountToFriend(AccountDto accountDto);

}
