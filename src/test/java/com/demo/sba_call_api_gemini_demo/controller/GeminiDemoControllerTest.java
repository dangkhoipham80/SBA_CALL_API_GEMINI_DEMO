package com.demo.sba_call_api_gemini_demo.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.sba_call_api_gemini_demo.dto.response.GeminiGenerateResponse;
import com.demo.sba_call_api_gemini_demo.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GeminiDemoController.class)
@ActiveProfiles("test")
class GeminiDemoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GeminiService geminiService;

  @Test
  void it_should_return_generate_result_when_prompt_is_valid() throws Exception {
    when(geminiService.generate(anyString()))
        .thenReturn(new GeminiGenerateResponse("gemini-2.5-flash", "P", "A"));

    mockMvc
        .perform(
            post("/api/v1/gemini/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"P\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(1000)))
        .andExpect(jsonPath("$.result.model", is("gemini-2.5-flash")))
        .andExpect(jsonPath("$.result.answer", is("A")));
  }

  @Test
  void it_should_return_bad_request_when_prompt_is_blank() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/gemini/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is(400)));
  }
}
