package ru.skillbox.dialogservice.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto dialogToDialogDto(Message dialog);
}
