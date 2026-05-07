package com.demo.sba_call_api_gemini_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SbaCallApiGeminiDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbaCallApiGeminiDemoApplication.class, args);
  }
}
