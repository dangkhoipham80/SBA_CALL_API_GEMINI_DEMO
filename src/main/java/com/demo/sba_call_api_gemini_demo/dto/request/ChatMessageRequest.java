package com.demo.sba_call_api_gemini_demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(@NotBlank String message) {}
