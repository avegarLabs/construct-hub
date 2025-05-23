package com.avegarlabs.construct_hub;

import com.avegarlabs.construct_hub.infrastructure.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConstructHubApplication {

	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(ConstructHubApplication.class, args);
	}

}
