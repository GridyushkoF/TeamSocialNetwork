package ru.skillbox.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class StorageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }
    @Value("${custom.rest.basePath}")
    private String apiPrefix;
    @Test
    void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "testFile.png",
                MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        mockMvc.perform(multipart(apiPrefix + "/storage")
                        .file(file)
                        .param("type", "IMAGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").isNotEmpty());
    }
}