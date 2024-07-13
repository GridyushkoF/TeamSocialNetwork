package ru.skillbox.authentication.service.utils;

import org.hashids.Hashids;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;

import java.util.Map;

public class CryptoTool {

    private final Hashids hashids;

    public CryptoTool(String salt) {
        var minimalLength = 10;
        this.hashids = new Hashids(salt, minimalLength);
    }

//    public String hashOf(Long value) {
//        return hashids.encode(value);
//    }
//
//    public Long idOf(String value) {
//        long[] res = hashids.decode(value);
//        if (res != null && res.length > 0) {
//            return res[0];
//        }
//        return null;
//    }

    public String encode(long temp, long id) {
        return hashids.encode(temp, id);
    }

    public Map<Long, Long> decode(String encodedString) {
        long[] decodedValues = hashids.decode(encodedString);
        if (decodedValues.length == 2) {
            return Map.of(decodedValues[0], decodedValues[1]);
        } else {
            throw new IncorrectRecoveryLinkException("Неверный формат закодированной строки");
        }
    }
}
