package ru.skillbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.model.Country;
import ru.skillbox.model.CountryDetailed;
import ru.skillbox.service.HhApiService;

import java.util.List;

@RestController
@RequestMapping("/hh-api")
@RequiredArgsConstructor
public class HhApiController {
    private final HhApiService hhApiService;

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getCountries() throws JsonProcessingException {
        return ResponseEntity.ok(hhApiService.getCountriesFromHhRu());
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<CountryDetailed> getCountry(@PathVariable Integer id) throws JsonProcessingException {
        return ResponseEntity.ok(hhApiService.getCountryFromHhRu(id));
    }


}
