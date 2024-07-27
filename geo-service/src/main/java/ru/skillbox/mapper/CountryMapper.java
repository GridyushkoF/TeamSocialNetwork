package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.model.dto.CountryResponse;
import ru.skillbox.model.dto.json.CountryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    @Mapping(source = "name", target = "title")
    CountryResponse dtoToResponse(CountryDto countryDto);

    List<CountryResponse> dtoListToResponseList(List<CountryDto> countryDtoList);
}
