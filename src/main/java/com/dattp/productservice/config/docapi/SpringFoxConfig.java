package com.dattp.productservice.config.docapi;

import com.dattp.productservice.anotation.docapi.AddAuthorizedDocAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(new ApiInfoBuilder()
            .title("Product restaurant service documentations")
            .description("Product restaurant service documentations")
            .contact(new Contact("Dat mars", "dattp.com", "dattp@gmailcom"))
            .version("0.0.1")
            .build()
        )
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.dattp.productservice.controller"))
        .paths(PathSelectors.ant("/api/**"))
        .build()
        .globalRequestParameters(parameters())
        .produces(new HashSet<>(List.of(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)));
  }

  private List<RequestParameter> parameters() {
    return null;
  }

  @Bean
  public OperationBuilderPlugin customOperationParameterPlugin() {
    return new OperationBuilderPlugin() {
      @Override
      public void apply(OperationContext context) {
        // Check if the API method or controller class has the @AddAuthorizedDocAPI annotation
        if (context.findAnnotation(AddAuthorizedDocAPI.class).isPresent()) {
          // Add Authorization header
          context.operationBuilder().requestParameters(
              List.of(new RequestParameterBuilder()
                  .name("access_token")
                  .required(true)
                  .in("header")
                  .description("Access token")
                  .build()
              )
          );
        }
      }

      @Override
      public boolean supports(DocumentationType delimiter) {
        return true;
      }
    };
  }
}