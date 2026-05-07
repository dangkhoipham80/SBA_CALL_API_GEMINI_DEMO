package com.demo.sba_call_api_gemini_demo.dto.response;

public record ChatTurnResponse(ChatMessageResponse userMessage, ChatMessageResponse botReply) {}
