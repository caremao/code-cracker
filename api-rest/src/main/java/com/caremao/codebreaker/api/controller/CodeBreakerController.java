package com.caremao.codebreaker.api.controller;

import com.caremao.codebreaker.api.model.SelectionRequest;
import com.caremao.codebreaker.api.model.WordMatchResponse;
import com.caremao.codebreaker.api.model.WordRequest;
import com.caremao.codebreaker.api.word.DataManager;
import com.caremao.codebreaker.api.word.WordValidationException;
import com.caremao.codebreaker.core.model.Word;
import com.caremao.codebreaker.core.processing.WordProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codebreaker/v1")
public class CodeBreakerController {

    private final WordProcessor wordProcessor;
    private final DataManager dataManager;

    public CodeBreakerController(WordProcessor wordProcessor, DataManager dataManager) {
        this.wordProcessor = wordProcessor;
        this.dataManager = dataManager;
    }

    @PostMapping("/words")
    @Operation(summary = "Adds a new word to the list")
    @ApiResponse(responseCode = "200", description = "Word added")
    @ApiResponse(responseCode = "400", description = "Invalid word")
    public ResponseEntity<Void> addWord(@RequestBody WordRequest request) {
        dataManager.addWord(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/words")
    @Operation(summary = "Returns the current word list")
    public ResponseEntity<List<WordMatchResponse>> listWords() {
        return ResponseEntity.ok(processList(dataManager.getWords()));
    }

    @DeleteMapping("/words/{word}")
    @Operation(summary = "Removes a word from the list")
    @ApiResponse(responseCode = "200", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Word not found")
    public ResponseEntity<Void> deleteWord(@PathVariable("word") String word) {
        dataManager.removeWord(word);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select")
    @Operation(summary = "Selects a word and number of matches")
    @ApiResponse(responseCode = "200", description = "Selection successful")
    @ApiResponse(responseCode = "404", description = "Error while selecting word")
    public ResponseEntity<List<WordMatchResponse>> selectWord(@RequestBody SelectionRequest request) {
        List<String> filteredList = wordProcessor.filterByFeedbackRaw(dataManager.getWords(), request.word(), request.hits()).stream()
                .map(Word::getText)
                .toList();
        return ResponseEntity.ok(processList(dataManager.replaceWords(filteredList)));
    }

    @PostMapping("/reset")
    @Operation(summary = "Restarts the game")
    public ResponseEntity<List<WordMatchResponse>> reset() {
        dataManager.resetWords();
        return ResponseEntity.ok(processList(dataManager.getWords()));
    }

    private List<WordMatchResponse> processList(List<String> inputWords) {
        return wordProcessor.processWords(inputWords).stream()
                .map(word -> new WordMatchResponse(word.getText(), word.getScore()))
                .toList();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWordNotFound(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(WordValidationException.class)
    public ResponseEntity<String> handleWordNotFound(WordValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
