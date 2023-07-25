package vn.iodev.humanresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HumanResourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourcesApplication.class, args);
	}

}
