package ru.skillbox.authentication.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.security.AppUserDetails;
import ru.skillbox.authentication.repository.sql.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Testcontainers
class UserDetailsServiceImplIT extends TestDependenciesContainer {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Load user by username - user exists, success")
    void testLoadUserByUsername_correct_success() {
        User testUser = saveTestUserAccountInDbAndGet();
        AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(testUser.getEmail());
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername());
        assertEquals(testUser.getId(), userDetails.getId());
    }

    @Test
    @DisplayName("Load user by username - email does not exist, throws EntityNotFoundException")
    void testLoadUserByUsername_notExistsEmail_error() {
        String nonExistentEmail = "nonexistent@mail.com";
        assertThrows(EntityNotFoundException.class, () -> userDetailsService.loadUserByUsername(nonExistentEmail));
    }
}