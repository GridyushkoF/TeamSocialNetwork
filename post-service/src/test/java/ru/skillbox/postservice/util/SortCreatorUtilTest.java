package ru.skillbox.postservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

public class SortCreatorUtilTest {
    @Test
    void createSort_Sorted() {
        boolean isSorted = true;
        boolean isUnsorted = false;
        boolean isEmpty = false;
        Sort sort = SortCreatorUtil.createSort(isEmpty,isSorted,isUnsorted);
        assertEquals(isSorted,sort.isSorted());
        assertEquals(isUnsorted,sort.isUnsorted());
        assertEquals(isEmpty,sort.isEmpty());
    }
    @Test
    void createSort_Empty() {
        boolean isSorted = true;
        boolean isUnsorted = true;
        boolean isEmpty = false;
        Sort sort = SortCreatorUtil.createSort(isEmpty,isSorted,isUnsorted);
        assertEquals(isEmpty,sort.isEmpty());
    }
    @Test
    void createSort_Unsorted() {
        boolean isSorted = false;
        boolean isUnsorted = true;
        boolean isEmpty = false;
        Sort sort = SortCreatorUtil.createSort(isEmpty,isSorted,isUnsorted);
        assertEquals(isUnsorted,sort.isUnsorted());
    }
}
