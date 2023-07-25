package vn.iodev.contestmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ContestManagementSystemApplication {

	public static void main(String[] args) {
		String directory = System.getProperty("user.dir");
		System.out.println(directory);
		SpringApplication.run(ContestManagementSystemApplication.class, args);
	}
}
