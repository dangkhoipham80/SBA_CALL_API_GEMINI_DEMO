package com.demo.sba_call_api_gemini_demo.repository;

import com.demo.sba_call_api_gemini_demo.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {}
