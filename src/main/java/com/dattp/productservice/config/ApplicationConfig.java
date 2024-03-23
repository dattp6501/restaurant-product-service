package com.dattp.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class ApplicationConfig {
    // @Bean
	// public RestTemplate getRestTemplate(){
	// 	return new RestTemplate();
	// }
	// @Value("${com.dattp.globalconfig.state.default}")

	public static final String[] pathPublic= {
        "/api/product/user/table/get_table",
		"/api/product/user/dish/get_dish",
		"/api/product/user/dish/get_dish_detail/*"
    };

	@Bean
	public RestTemplate cRestTemplate(){
		return new RestTemplate();
	}

}