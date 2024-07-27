package ru.skillbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.dto.CityDto;
import ru.skillbox.model.dto.CountryResponse;
import ru.skillbox.service.GeoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo")
@RequiredArgsConstructor
public class GeoController {
    private final GeoService geoService;

    @GetMapping("/country")
    public ResponseEntity<List<CountryResponse>> getCountries() throws JsonProcessingException {
        return ResponseEntity.ok(geoService.getCountries());
    }

    @GetMapping("/country/{id}/city")
    public ResponseEntity<List<CityDto>> getCountry(@PathVariable Integer id) throws JsonProcessingException {
        return ResponseEntity.ok(geoService.getCitiesByCountryId(id));
    }
}
