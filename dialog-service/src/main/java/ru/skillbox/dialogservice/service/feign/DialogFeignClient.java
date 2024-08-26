package ru.skillbox.dialogservice.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.dialogservice.model.dto.JwtRequest;

import java.util.Map;

@FeignClient(name = "dialog", url = "${app.feignClient.url}")
public interface DialogFeignClient {

    @PostMapping("/auth/getclaims")
    Map<String, String> validateToken(@RequestBody JwtRequest request);

    @GetMapping("/auth/close")
    void closeConnection(@RequestParam("id") Long id);
}
