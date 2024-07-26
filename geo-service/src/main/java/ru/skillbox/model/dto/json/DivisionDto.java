package ru.skillbox.model.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class DivisionDto implements Serializable {
    private int id;
    @JsonProperty("parent_id")
    private int parentId;
    private String name;
    private List<DivisionDto> areas;
}
