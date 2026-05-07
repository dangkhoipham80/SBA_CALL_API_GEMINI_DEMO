package com.demo.sba_call_api_gemini_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Gemini API Demo")
            .version("1.0.0")
            .description("Spring Boot app to demo Gemini Flash 2.5 API call")
            .contact(new Contact()
                .name("Demo")
                .url("https://github.com")));
  }
}
