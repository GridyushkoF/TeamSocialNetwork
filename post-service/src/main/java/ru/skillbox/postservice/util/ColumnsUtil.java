package ru.skillbox.postservice.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColumnsUtil {
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = src.getPropertyDescriptors();

        List<String> emptyNames = new ArrayList<>();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Object srcValue = src.getPropertyValue(descriptor.getName());
            if (srcValue == null) {
                emptyNames.add(descriptor.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}