package ru.skillbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.skillbox.mapper.CityMapper;
import ru.skillbox.mapper.CountryMapper;
import ru.skillbox.model.dto.CityDto;
import ru.skillbox.model.dto.CountryResponse;
import ru.skillbox.model.dto.json.DivisionDto;
import ru.skillbox.util.HttpUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoService {
    private static final String PREFIX_URL = "https://api.hh.ru";
    private final ObjectMapper objectMapper;
    private final HttpUtil httpUtil;
    private final CountryMapper countryMapper;

    @Cacheable(cacheNames = "cities")
    public List<CityDto> getCitiesByCountryId(int id) throws JsonProcessingException {
        String url = PREFIX_URL + "/areas/" + id;
        DivisionDto countryDto = objectMapper.readValue(httpUtil.sendHttpAndGetTextResponse(url),
                new TypeReference<>(){});
        return CityMapper.convertDivisionToCities(countryDto,countryDto.getId());
    }
    @Cacheable(cacheNames = "countries")
    public List<CountryResponse> getCountries() throws JsonProcessingException {
        String url = PREFIX_URL + "/areas/countries";
        return countryMapper.dtoListToResponseList(objectMapper.readValue(httpUtil.sendHttpAndGetTextResponse(url),
                new TypeReference<>(){}));
    }

}
