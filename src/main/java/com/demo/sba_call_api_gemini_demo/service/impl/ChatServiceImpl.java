package com.demo.sba_call_api_gemini_demo.service.impl;

import com.demo.sba_call_api_gemini_demo.dto.request.ChatCreateRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatMessageRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.GeminiApiRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatMessageResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatSessionResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatTurnResponse;
import com.demo.sba_call_api_gemini_demo.entity.ChatMessage;
import com.demo.sba_call_api_gemini_demo.entity.ChatSession;
import com.demo.sba_call_api_gemini_demo.repository.ChatMessageRepository;
import com.demo.sba_call_api_gemini_demo.repository.ChatSessionRepository;
import com.demo.sba_call_api_gemini_demo.repository.GeminiRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements com.demo.sba_call_api_gemini_demo.service.ChatService {

  private final ChatSessionRepository sessionRepository;
  private final ChatMessageRepository messageRepository;
  private final GeminiRepository geminiRepository;

  @Override
  @Transactional
  public ChatSessionResponse createSession(ChatCreateRequest request) {
    String title = (request.title() == null || request.title().isBlank()) ? "New Chat" : request.title();
    ChatSession session = ChatSession.builder().title(title).build();
    session = sessionRepository.save(session);
    return toSessionResponse(session, 0);
  }

  @Override
  public List<ChatSessionResponse> getSessions() {
    return sessionRepository.findAll().stream()
        .map(s -> toSessionResponse(s, messageRepository.findBySessionIdOrderByCreatedAtAsc(s.getId()).size()))
        .toList();
  }

  @Override
  @Transactional
  public ChatTurnResponse sendMessage(Long sessionId, ChatMessageRequest request) {
    ChatSession session = sessionRepository.findById(sessionId)
        .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));

    // Lưu tin nhắn của user
    ChatMessage userMsg = ChatMessage.builder()
        .session(session)
        .role("user")
        .content(request.message())
        .build();
    userMsg = messageRepository.save(userMsg);

    // Lấy toàn bộ lịch sử (bao gồm message vừa lưu) để gửi cho Gemini
    List<ChatMessage> history = messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    List<GeminiApiRequest.Content> contents = history.stream()
        .map(msg -> GeminiApiRequest.Content.builder()
            .role(msg.getRole())
            .parts(List.of(GeminiApiRequest.Part.builder().text(msg.getContent()).build()))
            .build())
        .toList();

    String botText = geminiRepository.generateContent(contents);

    // Lưu tin nhắn của bot
    ChatMessage botMsg = ChatMessage.builder()
        .session(session)
        .role("model")
        .content(botText)
        .build();
    botMsg = messageRepository.save(botMsg);

    return new ChatTurnResponse(toMessageResponse(userMsg), toMessageResponse(botMsg));
  }

  @Override
  public List<ChatMessageResponse> getMessages(Long sessionId) {
    if (!sessionRepository.existsById(sessionId)) {
      throw new EntityNotFoundException("Session not found: " + sessionId);
    }
    return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
        .map(this::toMessageResponse)
        .toList();
  }

  private ChatSessionResponse toSessionResponse(ChatSession session, int count) {
    return new ChatSessionResponse(session.getId(), session.getTitle(), session.getCreatedAt(), count);
  }

  private ChatMessageResponse toMessageResponse(ChatMessage msg) {
    return new ChatMessageResponse(msg.getId(), msg.getRole(), msg.getContent(), msg.getCreatedAt());
  }
}
