package com.caremao.codebreaker.core.processing;

import com.caremao.codebreaker.core.model.Letter;
import com.caremao.codebreaker.core.model.Word;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WordProcessor {

    public List<Word> processWords(final List<String> input) {
        if (Objects.isNull(input) || input.isEmpty()) {
            throw new IllegalArgumentException("Input should not be empty");
        }
        return orderWords(weightWords(
                input.stream().map(word -> Word
                                .builder()
                                .text(word)
                                .letters(parseChars(word))
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new))
        ));

    }

    private List<Letter> parseChars(final String source) {
        Objects.requireNonNull(source, "Line cannot be empty");
        return source
                .codePoints()
                .mapToObj(c -> (char) c)
                .map(character -> Letter
                        .builder()
                        .text(character)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Word> weightWords(final List<Word> input) {
        // Check size for word
        // Validate that all words are same size (fail otherwise)
        Set<Integer> sizes = input.stream()
                .map(Word::getText)
                .map(String::length)
                .collect(Collectors.toSet());
        Optional<Integer> optWordSize = sizes.stream().findFirst();
        if (optWordSize.isEmpty() || sizes.size() != 1) {
            throw new IllegalArgumentException("All words should be of the same size.");
        }
        int wordSize = optWordSize.get();

        List<Map<Character, Long>> characterOccurrences = IntStream.range(0, wordSize)
                .mapToObj(index ->
                        input.stream()
                                .map(Word::getLetters)
                                .map(letters -> letters.get(index))
                                .map(Letter::getText)
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                )
                .toList();

        // Weight each letter
        input.forEach(word -> {
            Long totalScore = 0L;
            List<Letter> letters = word.getLetters();
            for (int index = 0; index < letters.size(); index++) {
                Letter letter = letters.get(index);
                Long score = characterOccurrences.get(index).get(letter.getText());
                totalScore += score;
                letter.setScore(score);
            }
            word.setScore(totalScore);
            word.setSize(wordSize);
        });
        return input;
    }

    public List<Word> orderWords(final List<Word> input) {
        List<Word> ordered = new ArrayList<>(input);
        ordered.sort(Comparator.comparing(Word::getScore).reversed());
        return ordered;
    }
}
