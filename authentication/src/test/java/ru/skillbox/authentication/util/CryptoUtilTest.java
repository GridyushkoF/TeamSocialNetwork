package ru.skillbox.authentication.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilTest {

    private CryptoUtil cryptoUtil;

    @BeforeEach
    void setUp() {
        String salt = "hdsllLkjhHvKkjhgxgfdGkLGJfyfXFzsSgrT89JCgfDFtfggGyGFgfc";
        cryptoUtil = new CryptoUtil(salt);
    }

    @Test
    @DisplayName("test encoding")
    void testEncodeWithTemp() {
        long temp = 1234567890L;
        long id = 123L;

        String encodedString = cryptoUtil.encodeWithTemp(temp, id);

        assertNotNull(encodedString);
        assertFalse(encodedString.isEmpty());
    }

    @Test
    @DisplayName("test decoding")
    void testDecodeWithTemp_Success() {
        long temp = 1234567890L;
        long id = 123L;

        String encodedString = cryptoUtil.encodeWithTemp(temp, id);

        Map<Long, Long> decodedValues = cryptoUtil.decodeWithTemp(encodedString);

        assertEquals(temp, decodedValues.keySet().iterator().next());
        assertEquals(id, decodedValues.values().iterator().next());
    }

    @Test
    @DisplayName("test incorrect format decoding")
    void testDecodeWithTemp_IncorrectFormat() {
        String incorrectString = "incorrect-string";

        assertThrows(IncorrectRecoveryLinkException.class, () -> cryptoUtil.decodeWithTemp(incorrectString));
    }
}