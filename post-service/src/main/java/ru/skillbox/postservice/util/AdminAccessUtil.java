package ru.skillbox.postservice.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.skillbox.postservice.exception.AdminAccessException;


public class AdminAccessUtil {
    public static void throwExceptionIfTokenNotAdmin(HttpServletRequest request) {
        boolean isAdmin = request.getHeader("authorities").toUpperCase().contains("ADMIN");
        if (!isAdmin) {
            throw new AdminAccessException();
        }
    }
}
