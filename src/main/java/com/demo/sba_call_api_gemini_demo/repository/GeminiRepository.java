package com.demo.sba_call_api_gemini_demo.repository;

import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest;
import java.util.List;

public interface GeminiRepository {
  String generateContent(String prompt);
  String generateContent(List<GeminiApiRequest.Content> contents);
}
