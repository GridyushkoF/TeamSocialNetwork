package ru.skillbox.postservice.util;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import ru.skillbox.commonlib.util.SortCreatorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SortCreatorUtilTest {

    @Test
    public void createSort_ValidInputAscending_ReturnsSort() {
        List<String> sort = Arrays.asList("name", "ASC");
        Sort result = SortCreatorUtil.createSort(sort);

        assertEquals(Sort.Direction.ASC, result.getOrderFor("name").getDirection());
        assertEquals("name", result.getOrderFor("name").getProperty());
    }

    @Test
    public void createSort_ValidInputDescending_ReturnsSort() {
        List<String> sort = Arrays.asList("age", "DESC");
        Sort result = SortCreatorUtil.createSort(sort);

        assertEquals(Sort.Direction.DESC, result.getOrderFor("age").getDirection());
        assertEquals("age", result.getOrderFor("age").getProperty());
    }

    @Test
    public void createSort_EmptyList_ThrowsException() {
        List<String> sort = Collections.emptyList();

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            SortCreatorUtil.createSort(sort);
        });

        assertTrue(exception instanceof IndexOutOfBoundsException);
    }

    @Test
    public void createSort_InsufficientElements_ThrowsException() {
        List<String> sort = Collections.singletonList("ASC");

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            SortCreatorUtil.createSort(sort);
        });
        assertTrue(exception instanceof IndexOutOfBoundsException);
    }
}