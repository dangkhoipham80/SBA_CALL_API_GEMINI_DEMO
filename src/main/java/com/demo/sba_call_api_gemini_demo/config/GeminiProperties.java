package com.demo.sba_call_api_gemini_demo.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {
  @NotBlank private String apiKey;
  @NotBlank private String model;
  @NotBlank private String baseUrl;
  @Min(1)
  private Integer timeoutSeconds;
}
