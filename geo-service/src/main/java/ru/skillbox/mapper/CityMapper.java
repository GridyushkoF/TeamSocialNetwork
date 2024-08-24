package ru.skillbox.mapper;

import ru.skillbox.model.dto.CityDto;
import ru.skillbox.model.dto.json.DivisionDto;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {
    public static List<CityDto> convertDivisionToCities(DivisionDto divisionDto, int countryId) {
        List<CityDto> cities = new ArrayList<>();
        for (DivisionDto division : divisionDto.getAreas()) {
            if (isDivisionCity(division)) {
                CityDto cityDto = convertDivisionToCity(countryId, division);
                cities.add(cityDto);
                continue;
            }
            cities.addAll(convertDivisionToCities(division, countryId));
        }
        return cities;
    }

    private static CityDto convertDivisionToCity(int countryId, DivisionDto division) {
        return CityDto.builder()
                .countryId(countryId)
                .title(division.getName())
                .id(division.getId())
                .build();
    }

    private static boolean isDivisionCity(DivisionDto division) {
        return division.getAreas().isEmpty();
    }
    private CityMapper() {

    }
}
