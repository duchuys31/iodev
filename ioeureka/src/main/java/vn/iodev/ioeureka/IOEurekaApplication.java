package vn.iodev.ioeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IOEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOEurekaApplication.class, args);
	}

}
