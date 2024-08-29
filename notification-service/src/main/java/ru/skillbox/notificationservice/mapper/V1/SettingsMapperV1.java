package ru.skillbox.notificationservice.mapper.V1;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.notificationservice.model.dto.NotificationSettingsDto;
import ru.skillbox.notificationservice.model.dto.SettingsDto;
import ru.skillbox.notificationservice.model.entity.Settings;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingsMapperV1 {
    @Mapping(source = "post", target = "enablePost")
    @Mapping(source = "postComment", target = "enablePostComment")
    @Mapping(source = "commentComment", target = "enableCommentComment")
    @Mapping(source = "friendRequest", target = "enableFriendRequest")
    @Mapping(source = "message", target = "enableMessage")
    @Mapping(source = "friendBirthday", target = "enableFriendBirthday")
    @Mapping(source = "sendEmailMessage", target = "enableSendEmailMessage")
    NotificationSettingsDto toDto(Settings settings);

    @Mapping(source = "currentUserId", target = "userId")
    Settings toEntity(Long currentUserId, SettingsDto settingsDto);
}
