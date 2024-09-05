package ru.skillbox.userservice.mapper.v1;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commonlib.dto.account.AccountDto;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.dto.FriendShortDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendMapperV1 {

    @Mapping(source = "id", target = "friendId")
    FriendDto accountToFriend(AccountDto accountDto);

    @Mapping(target = "previousStatusCode", source = "statusCode")
    @Mapping(target = "rating", constant = "1")
    @Mapping(source = "id", target = "friendId")
    FriendShortDto accountDtoToFriendShortDto(AccountDto accountDto);
}
