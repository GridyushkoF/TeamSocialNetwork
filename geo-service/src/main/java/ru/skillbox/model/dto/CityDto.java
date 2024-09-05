package ru.skillbox.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing a city")
public class CityDto implements Serializable {

    @Schema(description = "ID of the city")
    private int id;

    @Schema(description = "ID of the country")
    private int countryId;

    @Schema(description = "Title of the city")
    private String title;

}
