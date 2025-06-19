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
        return triageWords(
                input.stream().map(this::processSingleWord)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    private Word processSingleWord(String word) {
        return Word
                .builder()
                .text(word)
                .letters(parseChars(word))
                .build();
    }

    public List<Word> filterByFeedbackRaw(final List<String> input, String selection, int matches) {
        return filterByFeedback(processWords(input), processSingleWord(selection), matches);
    }

    public List<Word> filterByFeedback(final List<Word> input, String selection, int matches) {
        return filterByFeedback(input, processSingleWord(selection), matches);
    }

    public List<Word> filterByFeedback(final List<Word> input, Word selection, int matches) {
        List<Word> result = new ArrayList<>(input);
        List<Word> list = result.stream().filter(word -> similarity(word, selection) == matches).toList();
        return triageWords(list);
    }

    private List<Word> triageWords(List<Word> list) {
        return orderWords(weightWords(list));
    }

    private int similarity(Word selection, Word toCompare) {
        List<Letter> letters1 = selection.getLetters();
        List<Letter> letters2 = toCompare.getLetters();

        int minLength = Math.min(letters1.size(), letters2.size());

        int count = 0;
        for (int i = 0; i < minLength; i++) {
            Character c1 = letters1.get(i).getText();
            Character c2 = letters2.get(i).getText();
            if (c1.equals(c2)) {
                count++;
            }
        }

        return count;
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

    private List<Word> weightWords(final List<Word> input) {
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

    private List<Word> orderWords(final List<Word> input) {
        List<Word> ordered = new ArrayList<>(input);
        ordered.sort(Comparator.comparing(Word::getScore).reversed());
        return ordered;
    }
}
