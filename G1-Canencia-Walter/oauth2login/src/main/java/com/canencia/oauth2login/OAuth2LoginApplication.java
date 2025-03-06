package com.canencia.oauth2login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.canencia.oauth2login.Controller")

public class OAuth2LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2LoginApplication.class, args);
	}

}
