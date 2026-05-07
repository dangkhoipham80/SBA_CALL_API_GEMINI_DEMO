package com.demo.sba_call_api_gemini_demo.repository;

import com.demo.sba_call_api_gemini_demo.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
