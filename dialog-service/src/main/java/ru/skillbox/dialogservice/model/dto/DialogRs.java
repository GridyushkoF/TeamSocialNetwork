package ru.skillbox.dialogservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DialogRs {

    private List<MessageDto> content;
}


