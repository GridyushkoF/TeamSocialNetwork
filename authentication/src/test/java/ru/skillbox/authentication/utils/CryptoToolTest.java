package ru.skillbox.authentication.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;
import ru.skillbox.authentication.service.utils.CryptoTool;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CryptoToolTest {

    private CryptoTool cryptoTool;

    @BeforeEach
    public void setUp() {

        cryptoTool = new CryptoTool("someSalt");
    }

    @Test
    public void testEncode() {
        long temp = 12345L;
        long id = 67890L;

        String encodedString = cryptoTool.encode(temp, id);

        assertNotNull(encodedString);
        assertFalse(encodedString.isEmpty());
    }

    @Test
    public void testDecode_ValidString() {
        long temp = 12345L;
        long id = 67890L;
        String encodedString = cryptoTool.encode(temp, id);

        Map<Long, Long> decodedValues = cryptoTool.decode(encodedString);

        assertNotNull(decodedValues);
        assertEquals(1, decodedValues.size());


        assertTrue(decodedValues.containsKey(temp));
        assertEquals(id, decodedValues.get(temp));

    }

    @Test
    public void testDecode_InvalidString() {
        String invalidEncodedString = "invalidString";

        IncorrectRecoveryLinkException exception = assertThrows(
                IncorrectRecoveryLinkException.class,
                () -> cryptoTool.decode(invalidEncodedString)
        );
        assertEquals("Неверный формат закодированной строки", exception.getMessage());
    }
}