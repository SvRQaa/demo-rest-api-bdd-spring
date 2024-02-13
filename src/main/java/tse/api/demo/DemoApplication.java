package tse.api.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tse.api.demo.service.v1.ExchangeServiceTestHelper;

@SpringBootApplication(scanBasePackages = {"tse.api.demo"})
public class DemoApplication {

	private final ExchangeServiceTestHelper exchangeServiceTestHelper;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Autowired
	public DemoApplication(ExchangeServiceTestHelper exchangeServiceTestHelper) {
		this.exchangeServiceTestHelper = exchangeServiceTestHelper;
	}

}
