package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "hello";
	}

	@PostMapping("/random")
	public String random() {
		WebClient webClient = WebClient.builder()
				.baseUrl("https://backend-dot-doxsee-project.appspot.com")
				.build();
		String result = webClient.post()
				.uri("/books/random")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		return result;

	}
}
