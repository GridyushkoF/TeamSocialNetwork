package ru.skillbox.authentication.service.utils;

import org.hashids.Hashids;

public class CryptoTool {

    private final Hashids hashids;

    public CryptoTool(String salt) {
        var minimalLength = 10;
        this.hashids = new Hashids(salt, minimalLength);
    }

    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            System.out.println("Посмотреть кодировку плностью: " + res.toString());
            System.out.println("Посмотреть кодировку " + res[0]);
            return res[0];
        }
        return null;
    }
}
