package com.caremao.codebreaker.api.controller;

import com.caremao.codebreaker.core.CoreConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CodeBreakerController.class)
@Import(CoreConfiguration.class)
class CodeBreakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(post("/codebreaker/v1/reset"))
                .andExpect(status().isOk());
    }

    @Test
    void addWord_thenListReturnsWord() throws Exception {
        mockMvc.perform(post("/codebreaker/v1/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"TEST\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/codebreaker/v1/words"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].word").value("TEST"))
                .andExpect(jsonPath("$[0].matches").value(4));
    }

    @Test
    void deleteWord_removesWord() throws Exception {
        mockMvc.perform(post("/codebreaker/v1/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"TEST\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/codebreaker/v1/words/TEST"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/codebreaker/v1/words"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void selectWord_filtersByHits() throws Exception {
        mockMvc.perform(post("/codebreaker/v1/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"TEST\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/codebreaker/v1/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"TEAM\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/codebreaker/v1/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"TEST\",\"hits\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].word").value("TEST"));
    }
}
