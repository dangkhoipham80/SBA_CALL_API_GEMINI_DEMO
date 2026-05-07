package com.demo.sba_call_api_gemini_demo.dto.response;

import java.time.LocalDateTime;

public record ChatMessageResponse(Long id, String role, String content, LocalDateTime createdAt) {}
