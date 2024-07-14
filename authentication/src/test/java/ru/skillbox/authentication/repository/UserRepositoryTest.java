package ru.skillbox.authentication.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.authentication.model.entity.Role;
import ru.skillbox.authentication.model.entity.User;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepositoryJpa;

    @Test
    public void whenGetByEmail_thenReturnUser() {
        User user = User.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .email("petrov@users.com")
                .password("superpas99")
                .role(Role.ADMIN)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        String userEmail = user.getEmail();
        User gotUser = userRepositoryJpa.findByEmail(userEmail).get();

        assertThat(gotUser.getPassword())
                .isEqualTo(user.getPassword());
    }
}