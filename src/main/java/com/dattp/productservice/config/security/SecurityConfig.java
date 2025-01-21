package com.dattp.productservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
  public static final String[] pathPublic = {
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
      "/swagger-ui/**",
      "/api/product/user/**",
      "/api/product/manage/**"
  };
  @Autowired
  private JWTAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .antMatchers(pathPublic).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        )
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers.frameOptions().disable())
        .csrf(csrf -> csrf
            .ignoringAntMatchers("/**")
        )
//            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
    ;
    return http.build();
  }

//  @Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//        .allowedOrigins("*")
//        .allowedMethods("GET", "POST", "OPTIONS", "PUT")
//        .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
//                "Access-Control-Request-Headers")
//        .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//        .allowCredentials(true).maxAge(3600);
//			}
//		};
//	}

//   @Bean
//   public CorsConfigurationSource corsConfigurationSource() {
//       CorsConfiguration configuration = new CorsConfiguration();
//       configuration.addAllowedOrigin("*"); // Cho phép tất cả các origin
//       configuration.addAllowedMethod("*"); // Cho phép tất cả các phương thức (GET, POST, PUT, DELETE, v.v.)
//       configuration.addAllowedHeader("*"); // Cho phép tất cả các header
//       configuration.setAllowCredentials(true); // Cho phép gửi cookie
//
//       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//       source.registerCorsConfiguration("/**", configuration);
//       return source;
//   }
}