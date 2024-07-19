package ru.skillbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.JSONParser;
import org.json.JSONString;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Collections;
import java.util.Iterator;

public class HhRuTest {
    public static void main(String[] args) throws JsonProcessingException {
        //JSONString jsonString = getDataFromHhRu();
        JSONObject jsonObject = getDataFromHhRu();
        //getCountries(jsonString);
        //redisTest();
    }

    private static void redisTest(){
        String host = "localhost";
        //String host = "95.174.93.240";
        int port = 6379;

        Jedis jedis = new Jedis(host, port);

        String[] myKeys = new String[3];
        for(int i = 0;i < myKeys.length;i++){
            myKeys[i] = "MyKeyName0" + String.valueOf(i);
        }
        /*myKeys[0] = "MyKeyName01";
        myKeys[1] = "MyKeyName02";
        myKeys[2] = "MyKeyName03";*/
        jedis.set(myKeys[0], "value01");
        jedis.set(myKeys[1], "17");
        jedis.set(myKeys[2], "12-march");

        for(String key: myKeys){
            System.out.println("Ключ = " + key + ", Значение = " + jedis.get(key));
        }
    }

    private static /*JSONString*/JSONObject getDataFromHhRu() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "https://api.hh.ru/areas/countries";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        //HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result =
                restTemplate.exchange(uri, HttpMethod.GET, null, /*Gson.class*/String.class/*JSONString.class*//*JSONObject.class*/);


        String myStringJson = "{\"id\":\"2354\",\"name\":\"Андорра\",\"url\":\"https://api.hh.ru/areas/2354\"}";
        //String myStringJson = result.getBody().toString();

        System.out.println("myStringJson = " + myStringJson);

        Country country = new Country();
        ObjectMapper objectMapper = new ObjectMapper();
        country = objectMapper.readValue(myStringJson,Country.class);

        System.out.println("id = " + country.getId());
        System.out.println("name = " + country.getName());
        System.out.println("url = " + country.getUrl());

        JSONObject jsonObj = new JSONObject(objectMapper.readValue(myStringJson,Country.class/*String.class*/));
        return jsonObj;
    }

    private static void getCountries(JSONString jsonString){
        // Считываем json
        //Object obj = new JSONParser().parse(jsonString); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
// Кастим obj в JSONObject
        //JSONObject jsonObject = (JSONObject) obj;
// Достаём firstName and lastName

        JSONObject jsonObject = (JSONObject) jsonString;
        //jsonString.

        String firstName = (String) jsonObject.get("firstName");
        String lastName = (String) jsonObject.get("lastName");
        System.out.println("fio: " + firstName + " " + lastName);
// Достаем массив номеров
        JSONArray phoneNumbersArr = (JSONArray) jsonObject.get("phoneNumbers");
        Iterator phonesItr = phoneNumbersArr.iterator();
        System.out.println("phoneNumbers:");
// Выводим в цикле данные массива
        while (phonesItr.hasNext()) {
            JSONObject test = (JSONObject) phonesItr.next();
            System.out.println("- type: " + test.get("type") + ", phone: " + test.get("number"));
        }
    }



}
