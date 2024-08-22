package ru.skillbox.userservice.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import ru.skillbox.commonlib.util.ColumnsUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

@UtilityClass
public class BeanUtil {

    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        String[] nullPropertyNames = ColumnsUtil.getNullPropertyNames(source);
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper dest = new BeanWrapperImpl(destination);
        for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
            String propertyName = descriptor.getName();
            if (!ArrayUtils.contains(nullPropertyNames, propertyName)) {
                Object value = src.getPropertyValue(propertyName);
                dest.setPropertyValue(propertyName, value);
            }
        }
    }
}