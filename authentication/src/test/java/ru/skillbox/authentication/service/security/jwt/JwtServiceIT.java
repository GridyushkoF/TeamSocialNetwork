package ru.skillbox.authentication.service.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Testcontainers
class JwtServiceIT extends TestDependenciesContainer {

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Generate JWT token - success")
    void testGenerateJwtToken() {
        String token = getTokenOfUser();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Get all claims from token - success")
    void testGetAllClaimsFromToken() {
        User user = saveTestUserAccountInDbAndGet();
        AppUserDetails userDetails = new AppUserDetails(user);
        String token = jwtService.generateJwtToken(userDetails);
        Claims claims = jwtService.getAllClaimsFromToken(token);
        assertEquals(user.getEmail(), claims.getSubject());
        assertEquals(user.getId().intValue(), claims.get("id"));
        assertEquals("[ADMIN]", claims.get("authorities"));
    }

    private String getTokenOfUser() {
        User user = saveTestUserAccountInDbAndGet();
        AppUserDetails userDetails = new AppUserDetails(user);
        return jwtService.generateJwtToken(userDetails);
    }
}