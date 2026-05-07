package com.demo.sba_call_api_gemini_demo.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class GeminiApiResponseTest {

  @Test
  void it_should_extract_first_text_when_response_has_candidate_part() {
    GeminiApiResponse response =
        new GeminiApiResponse(
            List.of(new GeminiApiResponse.Candidate(new GeminiApiResponse.Content(List.of(new GeminiApiResponse.Part("ok"))))));

    assertEquals("ok", response.firstText());
  }

  @Test
  void it_should_return_null_when_response_has_no_candidates() {
    GeminiApiResponse response = new GeminiApiResponse(List.of());
    assertNull(response.firstText());
  }
}
