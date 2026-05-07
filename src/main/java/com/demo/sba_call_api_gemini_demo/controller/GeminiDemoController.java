package com.demo.sba_call_api_gemini_demo.controller;

import com.demo.sba_call_api_gemini_demo.dto.request.GeminiGenerateRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ApiResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.service.GeminiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
public class GeminiDemoController {
  private final GeminiService geminiService;

  @GetMapping("/health")
  public ApiResponse<String> health() {
    return ApiResponse.<String>builder().result("SBA_CALL_API_GEMINI_DEMO is running").build();
  }

  @PostMapping("/generate")
  public ApiResponse<GeminiGenerateResponse> generate(@Valid @RequestBody GeminiGenerateRequest request) {
    GeminiGenerateResponse result = geminiService.generate(request.prompt());
    return ApiResponse.<GeminiGenerateResponse>builder().result(result).build();
  }
}
