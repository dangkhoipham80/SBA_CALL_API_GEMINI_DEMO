package com.demo.sba_call_api_gemini_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id", nullable = false)
  private ChatSession session;

  /** "user" hoặc "model" */
  @Column(nullable = false)
  private String role;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
