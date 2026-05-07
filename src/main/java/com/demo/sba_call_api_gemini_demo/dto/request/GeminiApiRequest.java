package com.demo.sba_call_api_gemini_demo.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record GeminiApiRequest(
    List<Content> contents, GenerationConfig generationConfig, List<SafetySetting> safetySettings) {
  @Builder
  public record Content(String role, List<Part> parts) {}

  @Builder
  public record Part(String text) {}

  @Builder
  public record GenerationConfig(Double temperature, Integer maxOutputTokens) {}

  @Builder
  public record SafetySetting(String category, String threshold) {}
}
