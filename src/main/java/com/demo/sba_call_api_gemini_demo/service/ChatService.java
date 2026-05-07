package com.demo.sba_call_api_gemini_demo.service;

import java.util.List;

import com.demo.sba_call_api_gemini_demo.dto.request.ChatCreateRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatMessageRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatMessageResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatSessionResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatTurnResponse;

public interface ChatService {
  ChatSessionResponse createSession(ChatCreateRequest request);
  List<ChatSessionResponse> getSessions();
  ChatTurnResponse sendMessage(Long sessionId, ChatMessageRequest request);
  List<ChatMessageResponse> getMessages(Long sessionId);
}
