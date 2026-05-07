package com.demo.sba_call_api_gemini_demo.controller;

import com.demo.sba_call_api_gemini_demo.dto.request.ChatCreateRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatMessageRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ApiResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatMessageResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatSessionResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatTurnResponse;
import com.demo.sba_call_api_gemini_demo.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "Multi-turn chat with Gemini, lịch sử lưu H2")
public class ChatController {

  private final ChatService chatService;

  @PostMapping("/sessions")
  @Operation(summary = "Tạo session chat mới")
  public ApiResponse<ChatSessionResponse> createSession(@RequestBody(required = false) ChatCreateRequest request) {
    ChatCreateRequest req = request != null ? request : new ChatCreateRequest(null);
    return ApiResponse.<ChatSessionResponse>builder().result(chatService.createSession(req)).build();
  }

  @GetMapping("/sessions")
  @Operation(summary = "Lấy danh sách tất cả sessions")
  public ApiResponse<List<ChatSessionResponse>> getSessions() {
    return ApiResponse.<List<ChatSessionResponse>>builder().result(chatService.getSessions()).build();
  }

  @PostMapping("/sessions/{sessionId}/messages")
  @Operation(summary = "Gửi tin nhắn, nhận phản hồi từ Gemini")
  public ApiResponse<ChatTurnResponse> sendMessage(
      @PathVariable Long sessionId,
      @Valid @RequestBody ChatMessageRequest request) {
    return ApiResponse.<ChatTurnResponse>builder()
        .result(chatService.sendMessage(sessionId, request))
        .build();
  }

  @GetMapping("/sessions/{sessionId}/messages")
  @Operation(summary = "Lấy toàn bộ lịch sử chat của session")
  public ApiResponse<List<ChatMessageResponse>> getMessages(@PathVariable Long sessionId) {
    return ApiResponse.<List<ChatMessageResponse>>builder()
        .result(chatService.getMessages(sessionId))
        .build();
  }
}
