package ru.skillbox.userservice.service.specifiaction_api;

import ru.skillbox.userservice.model.entity.User;

import java.time.LocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.function.Predicate;

public class AccountPredicate {

    public static Predicate<User> checkFirstName(String name) {
        if (name == null || name.isBlank()) {
            return u -> true;
        }
        return u -> u.getFirstName().equals(name);
    }

    public static Predicate<User> checkLastName(String name) {
        if (name == null || name.isBlank()) {
            return u -> true;
        }
        return u -> u.getLastName().equals(name);
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
            before = u -> u.getBirthDate().isBefore(ChronoZonedDateTime.from(to.plusSeconds(1)));
        }
        if (from != null) {
            after = u -> u.getBirthDate().isAfter(ChronoZonedDateTime.from(from.minusSeconds(1)));
        }
        return before.and(after);
    }

    public static Predicate<User> checkAge(int from, int to) {
        Predicate<User> before = u -> true;
        Predicate<User> after = u -> true;
        if (to > 0) {
            before = u -> u.getBirthDate().isBefore(ChronoZonedDateTime.from(LocalDateTime.now().plusYears(to)));
        }
        if (from > 0) {
            after = u -> u.getBirthDate().isAfter(ChronoZonedDateTime.from(LocalDateTime.now().plusYears(from)));
        }
        return before.and(after);
    }
    private AccountPredicate() {

    }
}
