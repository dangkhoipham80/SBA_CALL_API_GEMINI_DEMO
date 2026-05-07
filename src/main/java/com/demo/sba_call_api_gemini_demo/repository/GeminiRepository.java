package com.demo.sba_call_api_gemini_demo.repository;

import java.util.List;

import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest;

public interface GeminiRepository {
  String generateContent(String prompt);
  String generateContent(List<GeminiApiRequest.Content> contents);
}
