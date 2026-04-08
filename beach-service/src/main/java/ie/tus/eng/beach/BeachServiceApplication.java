package ie.tus.eng.beach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// EnableFeignClients scans for @FeignClient interfaces so Spring can proxy them at startup
@SpringBootApplication @EnableFeignClients
public class BeachServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeachServiceApplication.class, args);
	}
}
