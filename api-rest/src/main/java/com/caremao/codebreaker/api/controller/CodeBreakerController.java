package com.caremao.codebreaker.api.controller;

import com.caremao.codebreaker.api.model.SelectionRequest;
import com.caremao.codebreaker.api.model.WordMatchResponse;
import com.caremao.codebreaker.api.model.WordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/codebreaker")
public class CodeBreakerController {

    @PostMapping("/words")
    @Operation(summary = "Agrega una nueva palabra a la lista")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Palabra agregada"),
            @ApiResponse(responseCode = "400", description = "Palabra no válida")
    })
    public ResponseEntity<Void> addWord(@RequestBody WordRequest request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select")
    @Operation(summary = "Selecciona una palabra y envía el número de aciertos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista filtrada"),
            @ApiResponse(responseCode = "404", description = "Palabra no existe")
    })
    public ResponseEntity<List<WordMatchResponse>> selectWord(@RequestBody SelectionRequest request) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/words")
    @Operation(summary = "Devuelve la lista ordenada de palabras con su número actual de coincidencias")
    public ResponseEntity<List<WordMatchResponse>> listWords() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/reset")
    @Operation(summary = "Reinicia el estado del juego")
    public ResponseEntity<List<WordMatchResponse>> reset() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @DeleteMapping("/words/{word}")
    @Operation(summary = "Elimina una palabra específica de la lista")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe")
    })
    public ResponseEntity<Void> deleteWord(@PathVariable String word) {
        return ResponseEntity.ok().build();
    }
}
