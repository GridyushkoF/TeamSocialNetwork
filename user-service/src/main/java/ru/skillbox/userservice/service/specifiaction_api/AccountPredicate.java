package ru.skillbox.userservice.service.specifiaction_api;

import ru.skillbox.userservice.model.entity.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.function.Predicate;

public class AccountPredicate {

    public static Predicate<User> checkFirstName(String name) {
        if (name == null || name.isBlank()) {
            return u -> true;
        }
        return u -> u.getFirstName() != null && u.getFirstName().equalsIgnoreCase(name);
    }

    public static Predicate<User> checkLastName(String name) {
        if (name == null || name.isBlank()) {
            return u -> true;
        }
        return u -> u.getLastName() != null && u.getLastName().equalsIgnoreCase(name);
    }

    public static Predicate<User> checkIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return u -> true;
        }
        return u -> ids.contains(u.getId());
    }

    public static Predicate<User> birthdayBetween(LocalDateTime from, LocalDateTime to) {
        Predicate<User> before = u -> true;
        Predicate<User> after = u -> true;
        if (to != null) {
            before = u -> u.getBirthDate() != null && u.getBirthDate().isBefore(ChronoZonedDateTime.from(to.plusSeconds(1)));
        }
        if (from != null) {
            after = u -> u.getBirthDate() != null && u.getBirthDate().isAfter(ChronoZonedDateTime.from(from.minusSeconds(1)));
        }
        return before.and(after);
    }

    public static Predicate<User> checkAge(Integer from, Integer to) {
        Predicate<User> before = u -> true;
        Predicate<User> after = u -> true;
        if (to != null && to > 0) {
            before = u -> u.getBirthDate() != null && u.getBirthDate().isAfter(ZonedDateTime.now().minusYears(to));
        }
        if (from != null && from > 0) {
            after = u -> u.getBirthDate() != null && u.getBirthDate().isBefore(ZonedDateTime.now().minusYears(from));
        }
        return before.and(after);
    }
    private AccountPredicate() {

    }

    public static Predicate<User> checkCountry(String country) {
        if (country == null || country.isBlank()) {
            return u -> true;
        }
        return u -> u.getCountry() != null && u.getCountry().equalsIgnoreCase(country.trim());
    }

    public static Predicate<User> checkCity(String city) {
        if (city == null || city.isBlank()) {
            return u -> true;
        }
        return u -> u.getCity() != null && u.getCity().equalsIgnoreCase(city.trim());
    }

    public static Predicate<User> checkIsDeleted(boolean isDeleted) {
        return u -> u.isDeleted() == isDeleted;
    }
}
