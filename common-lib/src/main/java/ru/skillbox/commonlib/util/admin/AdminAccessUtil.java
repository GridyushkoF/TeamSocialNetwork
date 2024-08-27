package ru.skillbox.commonlib.util.admin;

import jakarta.servlet.http.HttpServletRequest;
import ru.skillbox.commonlib.exception.AdminAccessException;


public class AdminAccessUtil {
    public static void throwExceptionIfTokenNotAdmin(HttpServletRequest request) {
        boolean isAdmin = request.getHeader("authorities").toUpperCase().contains("ADMIN");
        System.out.println(request);
        System.out.println(request.getHeader("authorities"));
        if (!isAdmin) {
            throw new AdminAccessException();
        }
    }
    private AdminAccessUtil() {

    }
}
