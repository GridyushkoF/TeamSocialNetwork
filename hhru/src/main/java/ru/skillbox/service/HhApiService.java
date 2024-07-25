package ru.skillbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.skillbox.model.Country;
import ru.skillbox.model.CountryDetailed;
import ru.skillbox.util.Redis;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HhApiService {
    private final String prefixUrl = "https://api.hh.ru";
    private final WebClient.Builder webClientBuilder;

    public CountryDetailed getCountryFromHhRu(int id) throws JsonProcessingException {
        String url = prefixUrl + "/areas/" + Integer.toString(id);//одна страна

        ObjectMapper objectMapper = new ObjectMapper();
        CountryDetailed countryDetailed = objectMapper.readValue(getStringFromJson(url), new TypeReference<>(){});

        return countryDetailed;
    }

    public List<Country> getCountriesFromHhRu() throws JsonProcessingException {
        String url = prefixUrl + "/areas/countries";//страны

        /*HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");*/

        ObjectMapper objectMapper = new ObjectMapper();
        List<Country> listCountry = objectMapper.readValue(getStringFromJson(url), new TypeReference<>(){});

        Redis.loadCountryToDBRedis(listCountry);
        Country country = Redis.getCountryById(149);//проверка
        System.out.println("***************************");
        System.out.println("id = " + country.getId());
        System.out.println("name = " + country.getName());
        System.out.println("url = " + country.getUrl());
        System.out.println("***************************");

        return listCountry;
    }

    private String getStringFromJson(String url){
        String str = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return str;
    }


}//public class HhApiService
