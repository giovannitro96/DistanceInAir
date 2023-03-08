package it.unimi.distanceinair.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ws.config.annotation.EnableWs;

@SpringBootApplication
@EnableWs
public class DistanceInAirServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistanceInAirServerApplication.class, args);
	}

}
