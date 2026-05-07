package com.demo.sba_call_api_gemini_demo.service;

import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;

public interface GeminiService {
  GeminiGenerateResponse generate(String prompt);
}
