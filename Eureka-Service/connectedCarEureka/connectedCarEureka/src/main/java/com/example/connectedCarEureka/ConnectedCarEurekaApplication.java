package com.example.connectedCarEureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ConnectedCarEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectedCarEurekaApplication.class, args);
	}

}
