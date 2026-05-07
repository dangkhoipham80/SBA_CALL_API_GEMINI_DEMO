package com.demo.sba_call_api_gemini_demo.demo;

import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiDemoRunner implements CommandLineRunner {
  private final GeminiService geminiService;

  @Value("${demo.startup-call-enabled:true}")
  private boolean startupCallEnabled;

  @Override
  public void run(String... args) {
    if (!startupCallEnabled) {
      return;
    }

    String samplePrompt = "Give me one short motivational quote for students.";
    GeminiGenerateResponse response = geminiService.generate(samplePrompt);
    log.info("Startup Gemini demo answer: {}", response.answer());
  }
}
