package com.demo.sba_call_api_gemini_demo.repository.impl;

import com.demo.sba_call_api_gemini_demo.config.GeminiProperties;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest.Content;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest.GenerationConfig;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest.Part;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest.SafetySetting;
import com.demo.sba_call_api_gemini_demo.dto.response.GeminiApiResponse;
import com.demo.sba_call_api_gemini_demo.repository.GeminiRepository;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
@RequiredArgsConstructor
public class GeminiRepositoryImpl implements GeminiRepository {
  private final @Qualifier("geminiRestClient") RestClient geminiRestClient;
  private final GeminiProperties geminiProperties;

  @Override
  public String generateContent(String prompt) {
    GeminiApiRequest request =
        GeminiApiRequest.builder()
            .contents(
                List.of(
                    Content.builder().role("user").parts(List.of(Part.builder().text(prompt).build())).build()))
            .generationConfig(GenerationConfig.builder().temperature(0.7).maxOutputTokens(2048).build())
            .safetySettings(
                List.of(
                    SafetySetting.builder()
                        .category("HARM_CATEGORY_HARASSMENT")
                        .threshold("BLOCK_NONE")
                        .build(),
                    SafetySetting.builder()
                        .category("HARM_CATEGORY_HATE_SPEECH")
                        .threshold("BLOCK_NONE")
                        .build()))
            .build();

    String uri =
        "/v1beta/models/"
            + geminiProperties.getModel()
            + ":generateContent?key="
            + geminiProperties.getApiKey();

    GeminiApiResponse response =
        geminiRestClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GeminiApiResponse.class);

    if (response == null || response.firstText() == null || Objects.requireNonNull(response.firstText()).isBlank()) {
      throw new IllegalStateException("Gemini returned empty answer");
    }
    return response.firstText();
  }

  @Override
  public String generateContent(List<GeminiApiRequest.Content> contents) {
    GeminiApiRequest request =
        GeminiApiRequest.builder()
            .contents(contents)
            .generationConfig(GenerationConfig.builder().temperature(0.7).maxOutputTokens(2048).build())
            .safetySettings(
                List.of(
                    SafetySetting.builder()
                        .category("HARM_CATEGORY_HARASSMENT")
                        .threshold("BLOCK_NONE")
                        .build(),
                    SafetySetting.builder()
                        .category("HARM_CATEGORY_HATE_SPEECH")
                        .threshold("BLOCK_NONE")
                        .build()))
            .build();

    String uri =
        "/v1beta/models/"
            + geminiProperties.getModel()
            + ":generateContent?key="
            + geminiProperties.getApiKey();

    GeminiApiResponse response =
        geminiRestClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GeminiApiResponse.class);

    if (response == null || response.firstText() == null || Objects.requireNonNull(response.firstText()).isBlank()) {
      throw new IllegalStateException("Gemini returned empty answer");
    }
    return response.firstText();
  }
}
