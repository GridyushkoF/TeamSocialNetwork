package ru.skillbox.postservice.util;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SortCreatorUtil {
    private static final int DIRECTION_SLOT = 1;
    private static final int COLUMN_SLOT = 0;
    public static Sort createSort(boolean empty, boolean sorted, boolean unsorted) {
        if (sorted) {
            return Sort.by(Sort.Direction.ASC,"id");
        }
        return Sort.unsorted();
    }
    public static Sort createSort(List<String> sort) {
        return Sort.by(Sort.Direction.fromString(sort.get(DIRECTION_SLOT)), sort.get(COLUMN_SLOT));
    }
}
