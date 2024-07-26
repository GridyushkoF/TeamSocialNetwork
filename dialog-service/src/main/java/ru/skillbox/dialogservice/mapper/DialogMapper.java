package ru.skillbox.dialogservice.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.entity.Dialog;

@Mapper(componentModel = "spring")
public interface DialogMapper {
    DialogDto toDialogDto(Dialog dialog);
}
