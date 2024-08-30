package ru.skillbox.dialogservice.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.dialogservice.model.dto.JwtRequest;
import ru.skillbox.dialogservice.repository.DialogRepository;
import ru.skillbox.dialogservice.service.feign.DialogFeignClient;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@WithUserDetails(value = "admin@socialnetwork.com")
class DialogControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DialogRepository dialogRepository;

    @MockBean
    private DialogFeignClient feignClient;

    @Value("${app.apiPrefix}")
    private String apiPrefix;

    @BeforeEach
    public void setUp() {
        dialogRepository.deleteAll();
    }

    @Test
    @DisplayName("test update dialog by id, dialog not exists, bad request")
    void testPutDialogById_dialogNotExists_badRequest() throws Exception {
        HashMap<String, String> tokenPayload = new HashMap<>();
        tokenPayload.put("id", "1");
        tokenPayload.put("authorities", "ADMIN");
        Mockito.when(feignClient.validateToken(new JwtRequest("token")))
                .thenReturn(tokenPayload);

        mockMvc.perform(MockMvcRequestBuilders.put(apiPrefix + "/dialogs/{id}", 145145145L)
                        .cookie(new Cookie("jwt", "token"))
                        .header("id", 1L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}