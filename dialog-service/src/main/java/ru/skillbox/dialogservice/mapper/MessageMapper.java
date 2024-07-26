package ru.skillbox.dialogservice.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.dialogservice.model.dto.MessageDto;
import ru.skillbox.dialogservice.model.entity.Message;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto toDto(Message dialog);

    List<MessageDto> toDtoList(List<Message> messages);

    Message toEntity(MessageDto messageDto);

}
