package com.dattp.productservice;

import com.dattp.productservice.service.TelegramService;
import com.dattp.productservice.utils.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.format.DateTimeFormatter;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
public class ProductServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunnerBean(TelegramService telegramService) {
		return (args) -> {
			String message =
				DateUtils.getcurrentLocalDateTime()
					.plusHours(7)
					.format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd"))
					+": PRODUCT ===> STARTED";
			telegramService.sendNotificatonMonitorSystem(message);
		};
	}
}