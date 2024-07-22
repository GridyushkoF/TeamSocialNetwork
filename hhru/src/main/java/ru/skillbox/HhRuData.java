package ru.skillbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class HhRuData {
    public static void main(String[] args) throws JsonProcessingException {
        String host = "localhost";
        //String host = "95.174.93.240";
        int port = 6379;

        List<Country> listCountry = getCountryFromHhRu();
        for(Country country:listCountry){
            System.out.println("id = " + country.getId());
            System.out.println("name = " + country.getName());
            System.out.println("url = " + country.getUrl());
            System.out.println("-----------------------------");
        }

        loadCountryToDBRedis(listCountry, host, port);
        Country country = getCountryById(149,host,port);
        System.out.println("***************************");
        System.out.println("id = " + country.getId());
        System.out.println("name = " + country.getName());
        System.out.println("url = " + country.getUrl());
        System.out.println("***************************");
    }

    private static void loadCountryToDBRedis(List<Country> listCountry, String host, int port){
        Jedis jedis = new Jedis(host, port);

        for(Country country:listCountry){
            String key = country.getId().toString();
            jedis.hset(key,"name", country.getName());
            jedis.hset(key,"url", country.getUrl());
        }
    }

    private static List<Country> getCountryFromHhRu() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "https://api.hh.ru/areas/countries";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        ResponseEntity<?> result =
                restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        String stringFromJson = result.getBody().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Country> listCountry = objectMapper.readValue(stringFromJson, new TypeReference<>(){});

        return listCountry;
    }

    private static Country getCountryById(int id, String host, int port){
        Jedis jedis = new Jedis(host, port);

        Country country = new Country();

        String key = Integer.toString(id);
        country.setId(id);
        country.setName(jedis.hget(key,"name"));
        country.setUrl(jedis.hget(key,"url"));

        return country;
    }


}
