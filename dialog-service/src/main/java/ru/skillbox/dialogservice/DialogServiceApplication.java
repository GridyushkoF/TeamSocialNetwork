package ru.skillbox.dialogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DialogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DialogServiceApplication.class, args);
	}

}
