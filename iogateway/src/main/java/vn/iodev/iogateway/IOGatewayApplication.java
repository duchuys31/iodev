package vn.iodev.iogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class IOGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOGatewayApplication.class, args);
	}

}
