package com.dattp.productservice.config.security;

import com.dattp.productservice.config.ApplicationConfig;
import com.dattp.productservice.config.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled=true,jsr250Enabled=true)
public class SecurityConfig{
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

  public static final String[] pathPublic= {
    "/isRunning",
    "/api/product/user/table",
    "/api/product/user/table/*",
    "/api/product/user/table/*/comment",
    "/api/product/user/dish",
    "/api/product/user/dish/*",
    "/api/product/user/dish/*/comment",
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/v2/api-docs",
    "/webjars/**",
    "/swagger-ui/**"
  };



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .antMatchers(pathPublic).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            )
            .headers(headers -> headers.frameOptions().disable())
            .csrf(csrf -> csrf
                .ignoringAntMatchers("/**")
            );
        return http.build();
    }

  @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}