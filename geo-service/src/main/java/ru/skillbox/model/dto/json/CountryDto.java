package ru.skillbox.model.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Schema(description = "DTO representing a country")
public class CountryDto implements Serializable {

    @Schema(description = "ID of the country")
    private int id;

    @Schema(description = "Name of the country")
    private String name;

}
