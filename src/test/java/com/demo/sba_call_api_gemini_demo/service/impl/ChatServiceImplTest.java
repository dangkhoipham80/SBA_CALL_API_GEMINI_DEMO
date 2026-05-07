package com.demo.sba_call_api_gemini_demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.demo.sba_call_api_gemini_demo.BaseUnitTest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatCreateRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatMessageRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatSessionResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatTurnResponse;
import com.demo.sba_call_api_gemini_demo.entity.ChatMessage;
import com.demo.sba_call_api_gemini_demo.entity.ChatSession;
import com.demo.sba_call_api_gemini_demo.repository.ChatMessageRepository;
import com.demo.sba_call_api_gemini_demo.repository.ChatSessionRepository;
import com.demo.sba_call_api_gemini_demo.repository.GeminiRepository;

import jakarta.persistence.EntityNotFoundException;

class ChatServiceImplTest extends BaseUnitTest {

  @Mock private ChatSessionRepository sessionRepository;
  @Mock private ChatMessageRepository messageRepository;
  @Mock private GeminiRepository geminiRepository;

  private ChatServiceImpl chatService;

  @BeforeEach
  void setUp() {
    chatService = new ChatServiceImpl(sessionRepository, messageRepository, geminiRepository);
  }

  // ── createSession ──────────────────────────────────────────────────────────

  @Test
  void it_should_create_session_with_given_title() {
    ChatSession saved = ChatSession.builder().id(1L).title("My Chat").build();
    when(sessionRepository.save(any())).thenReturn(saved);

    ChatSessionResponse response = chatService.createSession(new ChatCreateRequest("My Chat"));

    assertEquals(1L, response.id());
    assertEquals("My Chat", response.title());
  }

  @Test
  void it_should_use_default_title_when_title_is_null() {
    ChatSession saved = ChatSession.builder().id(2L).title("New Chat").build();
    when(sessionRepository.save(any())).thenReturn(saved);

    ChatSessionResponse response = chatService.createSession(new ChatCreateRequest(null));

    assertEquals("New Chat", response.title());
  }

  @Test
  void it_should_use_default_title_when_title_is_blank() {
    ChatSession saved = ChatSession.builder().id(3L).title("New Chat").build();
    when(sessionRepository.save(any())).thenReturn(saved);

    ChatSessionResponse response = chatService.createSession(new ChatCreateRequest("   "));

    assertEquals("New Chat", response.title());
  }

  // ── sendMessage ────────────────────────────────────────────────────────────

  @Test
  void it_should_send_message_and_return_both_turns() {
    ChatSession session = ChatSession.builder().id(1L).title("Test").build();
    when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

    ChatMessage userMsg = ChatMessage.builder()
        .id(10L).session(session).role("user").content("Hello")
        .createdAt(LocalDateTime.now()).build();
    ChatMessage botMsg = ChatMessage.builder()
        .id(11L).session(session).role("model").content("Hi!")
        .createdAt(LocalDateTime.now()).build();

    when(messageRepository.save(any()))
        .thenReturn(userMsg)
        .thenReturn(botMsg);
    when(messageRepository.findBySessionIdOrderByCreatedAtAsc(1L))
        .thenReturn(List.of(userMsg));
    when(geminiRepository.generateContent(any(List.class))).thenReturn("Hi!");

    ChatTurnResponse result = chatService.sendMessage(1L, new ChatMessageRequest("Hello"));

    assertEquals("user", result.userMessage().role());
    assertEquals("Hello", result.userMessage().content());
    assertEquals("model", result.botReply().role());
    assertEquals("Hi!", result.botReply().content());
    verify(geminiRepository).generateContent(any(List.class));
  }

  @Test
  void it_should_throw_when_session_not_found_on_send() {
    when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> chatService.sendMessage(99L, new ChatMessageRequest("Hi")));
  }

  // ── getMessages ────────────────────────────────────────────────────────────

  @Test
  void it_should_return_messages_for_existing_session() {
    when(sessionRepository.existsById(1L)).thenReturn(true);
    ChatSession session = ChatSession.builder().id(1L).title("T").build();
    ChatMessage msg = ChatMessage.builder()
        .id(1L).session(session).role("user").content("Hi")
        .createdAt(LocalDateTime.now()).build();
    when(messageRepository.findBySessionIdOrderByCreatedAtAsc(1L)).thenReturn(List.of(msg));

    var messages = chatService.getMessages(1L);

    assertEquals(1, messages.size());
    assertEquals("user", messages.getFirst().role());
  }

  @Test
  void it_should_throw_when_session_not_found_on_get_messages() {
    when(sessionRepository.existsById(99L)).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> chatService.getMessages(99L));
  }
}
