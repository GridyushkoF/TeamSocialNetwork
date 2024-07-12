package ru.skillbox;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        String host = "localhost";
        //String host = "95.174.93.240";
        int port = 6379;

        Jedis jedis = new Jedis(host, port);

        jedis.set("MyKeyName01", "value01");
        jedis.set("MyKeyName02", "17");

        String myKey = "MyKeyName01";
        System.out.println("Ключ = " + myKey + ", Значение = " + jedis.get(myKey));
        myKey = "MyKeyName02";
        System.out.println("Ключ = " + myKey + ", Значение = " + jedis.get(myKey));
    }
}
