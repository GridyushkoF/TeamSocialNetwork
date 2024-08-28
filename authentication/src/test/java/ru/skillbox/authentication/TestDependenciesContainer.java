package ru.skillbox.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.commonlib.dto.account.Role;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
public class TestDependenciesContainer {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveTestUserAccountInDbAndGet() {
        return userRepository.save(User.builder()
                .email("testMail@gmal.com")
                .password(passwordEncoder.encode("123"))
                .firstName("FIRSTNAME")
                .lastName("LASTNAME")
                .role(Role.ADMIN)
                .build());
    }
}
