package com.demo.sba_call_api_gemini_demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.sba_call_api_gemini_demo.BaseUnitTest;
import com.demo.sba_call_api_gemini_demo.config.GeminiProperties;
import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.repository.GeminiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class GeminiServiceImplTest extends BaseUnitTest {

  @Mock private GeminiRepository geminiRepository;

  private GeminiServiceImpl geminiService;

  @BeforeEach
  void setUp() {
    GeminiProperties properties = new GeminiProperties();
    properties.setModel("gemini-2.5-flash");
    geminiService = new GeminiServiceImpl(geminiRepository, properties);
  }

  @Test
  void it_should_return_mapped_response_when_repository_returns_answer() {
    when(geminiRepository.generateContent("hello")).thenReturn("hi from gemini");

    GeminiGenerateResponse result = geminiService.generate("hello");

    assertEquals("gemini-2.5-flash", result.model());
    assertEquals("hello", result.prompt());
    assertEquals("hi from gemini", result.answer());
    verify(geminiRepository).generateContent("hello");
  }
}
