package com.demo.sba_call_api_gemini_demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.sba_call_api_gemini_demo.dto.request.GeminiGenerateRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ApiResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.service.GeminiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
@Tag(name = "Gemini API", description = "Gemini Flash 2.5 API operations")
public class GeminiDemoController {
  private final GeminiService geminiService;

  @GetMapping("/health")
  @Operation(summary = "Health check", description = "Check if the service is running")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service is running")
  })
  public ApiResponse<String> health() {
    return ApiResponse.<String>builder().result("SBA_CALL_API_GEMINI_DEMO is running").build();
  }

  @PostMapping("/generate")
  @Operation(summary = "Generate content", description = "Generate content using Gemini Flash 2.5 API")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Content generated successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ApiResponse<GeminiGenerateResponse> generate(@Valid @RequestBody GeminiGenerateRequest request) {
    GeminiGenerateResponse result = geminiService.generate(request.prompt());
    return ApiResponse.<GeminiGenerateResponse>builder().result(result).build();
  }
}

