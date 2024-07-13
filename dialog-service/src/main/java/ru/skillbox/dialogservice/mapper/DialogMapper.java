package ru.skillbox.dialogservice.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.entity.Dialog;

@Mapper(componentModel = "spring")
@DecoratedWith(DialogMapperDecorator.class)
public interface DialogMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "conversationPartner", ignore = true)
    @Mapping(target = "lastMessage", ignore = true)
    DialogDto dialogToDialogDto(Dialog dialog);

}