package com.demo.sba_call_api_gemini_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.sba_call_api_gemini_demo.entity.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {}
