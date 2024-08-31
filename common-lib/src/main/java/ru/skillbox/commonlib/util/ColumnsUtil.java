package ru.skillbox.commonlib.util;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

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

    public static void copyNonNullProperties(Object source, Object target) {
        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors()) {
                Object value = propertyDescriptor.getReadMethod().invoke(source);
                if (value != null) {
                    BeanUtils.setProperty(target, propertyDescriptor.getName(), value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ColumnsUtil() {}
}