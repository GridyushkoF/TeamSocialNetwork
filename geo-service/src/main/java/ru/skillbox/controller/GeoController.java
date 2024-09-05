package ru.skillbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Geo Controller", description = "Geo API")
public class GeoController {
    private final GeoService geoService;

    @GetMapping("/country")
    @Operation(summary = "Get countries")
    public ResponseEntity<List<CountryResponse>> getCountries() throws JsonProcessingException {
        return ResponseEntity.ok(geoService.getCountries());
    }

    @GetMapping("/country/{id}/city")
    @Operation(summary = "Get country")
    public ResponseEntity<List<CityDto>> getCountry(@PathVariable Integer id) throws JsonProcessingException {
        return ResponseEntity.ok(geoService.getCitiesByCountryId(id));
    }
}
