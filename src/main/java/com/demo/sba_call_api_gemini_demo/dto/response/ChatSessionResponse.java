package com.demo.sba_call_api_gemini_demo.dto.response;

import java.time.LocalDateTime;

public record ChatSessionResponse(Long id, String title, LocalDateTime createdAt, int messageCount) {}
