package ru.skillbox.postservice.util;

import org.springframework.data.domain.Sort;

public class SortCreatorUtil {
    public static Sort createSort(boolean empty, boolean sorted, boolean unsorted) {
        if (sorted) {
            return Sort.by(Sort.Direction.ASC,"id");
        }
        return Sort.unsorted();
    }
}
