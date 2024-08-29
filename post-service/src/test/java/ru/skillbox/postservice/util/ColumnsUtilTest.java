package ru.skillbox.postservice.util;

import lombok.Data;
import org.junit.jupiter.api.Test;
import ru.skillbox.commonlib.util.ColumnsUtil;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ColumnsUtilTest {

    @Test
    void getNullPropertyNames() {
        TestObject testObject = new TestObject();
        testObject.setField1("Value 1");
        testObject.setField2(null);
        testObject.setField3(null);
        testObject.setField4("Value 4");

        String[] nullPropertyNames = ColumnsUtil.getNullPropertyNames(testObject);
        String[] expectedNullPropertyNames = {"field2", "field3"};
        assertArrayEquals(expectedNullPropertyNames, nullPropertyNames);
    }

   @Data
    public static class TestObject {
        private String field1;
        private String field2;
        private String field3;
        private String field4;
    }
}