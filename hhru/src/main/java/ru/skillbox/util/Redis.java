package ru.skillbox.util;

import redis.clients.jedis.Jedis;
import ru.skillbox.model.Country;

import java.util.List;

public class Redis {
    static String hostRedis = "localhost";
    //String hostRedis = "95.174.93.240";
    static int portRedis = 6379;
    //----------------------------------

    public static void loadCountryToDBRedis(List<Country> listCountry){
        Jedis jedis = new Jedis(hostRedis, portRedis);

        for(Country country:listCountry){
            String key = Integer.toString(country.getId());
            jedis.hset(key,"name", country.getName());
            jedis.hset(key,"url", country.getUrl());
        }
    }

    public static Country getCountryById(int id){
        Jedis jedis = new Jedis(hostRedis, portRedis);

        Country country = new Country();

        String key = Integer.toString(id);
        if(jedis.keys(key) != null){
            country.setId(id);
            country.setName(jedis.hget(key,"name"));
            country.setUrl(jedis.hget(key,"url"));
        }

        return country;
    }
    

}
