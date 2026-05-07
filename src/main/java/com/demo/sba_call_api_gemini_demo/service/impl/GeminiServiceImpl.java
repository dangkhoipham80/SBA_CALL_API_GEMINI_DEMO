package com.demo.sba_call_api_gemini_demo.service.impl;

import com.demo.sba_call_api_gemini_demo.config.GeminiProperties;
import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.repository.GeminiRepository;
import com.demo.sba_call_api_gemini_demo.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService {
  private final GeminiRepository geminiRepository;
  private final GeminiProperties geminiProperties;

  @Override
  public GeminiGenerateResponse generate(String prompt) {
    String answer = geminiRepository.generateContent(prompt);
    return new GeminiGenerateResponse(geminiProperties.getModel(), prompt, answer);
  }
}
