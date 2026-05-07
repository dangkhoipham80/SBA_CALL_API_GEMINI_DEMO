package com.demo.sba_call_api_gemini_demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.sba_call_api_gemini_demo.dto.request.ChatCreateRequest;
import com.demo.sba_call_api_gemini_demo.dto.request.ChatMessageRequest;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatMessageResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatSessionResponse;
import com.demo.sba_call_api_gemini_demo.dto.response.ChatTurnResponse;
import com.demo.sba_call_api_gemini_demo.service.ChatService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatController.class)
@ActiveProfiles("test")
class ChatControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private ChatService chatService;

  private static final LocalDateTime NOW = LocalDateTime.now();

  // ── POST /sessions ─────────────────────────────────────────────────────────

  @Test
  void it_should_create_session_and_return_201() throws Exception {
    when(chatService.createSession(any())).thenReturn(
        new ChatSessionResponse(1L, "My Chat", NOW, 0));

    mockMvc.perform(post("/api/v1/chat/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"My Chat\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(1000)))
        .andExpect(jsonPath("$.result.id", is(1)))
        .andExpect(jsonPath("$.result.title", is("My Chat")));
  }

  @Test
  void it_should_create_session_without_body() throws Exception {
    when(chatService.createSession(any())).thenReturn(
        new ChatSessionResponse(2L, "New Chat", NOW, 0));

    mockMvc.perform(post("/api/v1/chat/sessions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result.title", is("New Chat")));
  }

  // ── GET /sessions ──────────────────────────────────────────────────────────

  @Test
  void it_should_return_all_sessions() throws Exception {
    when(chatService.getSessions()).thenReturn(List.of(
        new ChatSessionResponse(1L, "Chat A", NOW, 2),
        new ChatSessionResponse(2L, "Chat B", NOW, 5)));

    mockMvc.perform(get("/api/v1/chat/sessions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", hasSize(2)))
        .andExpect(jsonPath("$.result[0].title", is("Chat A")))
        .andExpect(jsonPath("$.result[1].messageCount", is(5)));
  }

  // ── POST /sessions/{id}/messages ───────────────────────────────────────────

  @Test
  void it_should_send_message_and_return_turn_response() throws Exception {
    ChatMessageResponse userMsg = new ChatMessageResponse(1L, "user", "Hello", NOW);
    ChatMessageResponse botMsg  = new ChatMessageResponse(2L, "model", "Hi!", NOW);
    when(chatService.sendMessage(eq(1L), any(ChatMessageRequest.class)))
        .thenReturn(new ChatTurnResponse(userMsg, botMsg));

    mockMvc.perform(post("/api/v1/chat/sessions/1/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"message\":\"Hello\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result.userMessage.content", is("Hello")))
        .andExpect(jsonPath("$.result.botReply.content", is("Hi!")))
        .andExpect(jsonPath("$.result.botReply.role", is("model")));
  }

  @Test
  void it_should_return_bad_request_when_message_is_blank() throws Exception {
    mockMvc.perform(post("/api/v1/chat/sessions/1/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"message\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  // ── GET /sessions/{id}/messages ────────────────────────────────────────────

  @Test
  void it_should_return_history_for_session() throws Exception {
    when(chatService.getMessages(1L)).thenReturn(List.of(
        new ChatMessageResponse(1L, "user", "Hello", NOW),
        new ChatMessageResponse(2L, "model", "Hi!", NOW)));

    mockMvc.perform(get("/api/v1/chat/sessions/1/messages"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", hasSize(2)))
        .andExpect(jsonPath("$.result[0].role", is("user")))
        .andExpect(jsonPath("$.result[1].role", is("model")));
  }
}
