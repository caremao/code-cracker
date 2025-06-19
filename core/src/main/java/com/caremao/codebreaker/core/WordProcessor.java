package com.caremao.codebreaker.core;

import com.caremao.codebreaker.core.model.Letter;
import com.caremao.codebreaker.core.model.Word;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WordProcessor {

    public List<Word> processWords(List<String> input) {
        if (Objects.isNull(input) || input.isEmpty()) {
            throw new IllegalArgumentException("Input should not be empty");
        }
        return input.stream().map(word -> Word
                        .builder()
                        .text(word)
                        .letters(parseChars(word))
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Letter> parseChars(String source) {
        Objects.requireNonNull(source, "Line cannot be empty");
        return source
                .codePoints()
                .mapToObj(Character.class::cast)
                .map(character -> Letter
                        .builder()
                        .text(character)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Word> weightWords(List<Word> input) {
        return Collections.emptyList();
    }
}
