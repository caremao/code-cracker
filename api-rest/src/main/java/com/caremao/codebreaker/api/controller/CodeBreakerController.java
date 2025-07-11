package com.caremao.codebreaker.api.controller;

import com.caremao.codebreaker.api.model.SelectionRequest;
import com.caremao.codebreaker.api.model.WordMatchResponse;
import com.caremao.codebreaker.api.model.WordRequest;
import com.caremao.codebreaker.core.model.Word;
import com.caremao.codebreaker.core.processing.WordProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/codebreaker/v1")
public class CodeBreakerController {

    private final List<String> inputWords = new ArrayList<>();

    private final WordProcessor wordProcessor;

    public CodeBreakerController(WordProcessor wordProcessor) {
        this.wordProcessor = wordProcessor;
    }

    @PostMapping("/words")
    @Operation(summary = "Adds a new word to the list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Word added"),
            @ApiResponse(responseCode = "400", description = "Invalid word")
    })
    public ResponseEntity<Void> addWord(@RequestBody WordRequest request) {
        inputWords.add(request.word());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/words")
    @Operation(summary = "Returns the current word list")
    public ResponseEntity<List<WordMatchResponse>> listWords() {
        return ResponseEntity.ok(processList(inputWords));
    }

    @DeleteMapping("/words/{word}")
    @Operation(summary = "Removes a word from the list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Word not found")
    })
    public ResponseEntity<Void> deleteWord(@PathVariable String word) {
        inputWords.remove(word);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select")
    @Operation(summary = "Selects a word and number of matches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Selection successful"),
            @ApiResponse(responseCode = "404", description = "Error while selecting word")
    })
    public ResponseEntity<List<WordMatchResponse>> selectWord(@RequestBody SelectionRequest request) {
        inputWords.clear();

        inputWords.addAll(wordProcessor.filterByFeedbackRaw(inputWords, request.word(), request.hits()).stream()
                .map(Word::getText)
                .toList());
        return ResponseEntity.ok(processList(inputWords));
    }

    @PostMapping("/reset")
    @Operation(summary = "Restarts the game")
    public ResponseEntity<List<WordMatchResponse>> reset() {
        inputWords.clear();
        return ResponseEntity.ok(processList(inputWords));
    }

    private List<WordMatchResponse> processList(List<String> inputWords) {
        return wordProcessor.processWords(inputWords).stream()
                .map(word -> new WordMatchResponse(word.getText(), word.getScore()))
                .toList();
    }
}
