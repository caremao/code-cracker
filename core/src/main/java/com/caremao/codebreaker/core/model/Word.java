package com.caremao.codebreaker.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
public class Word {

    @NonNull
    private String text;
    @NonNull
    private List<Letter> letters;
    private int size;
    private long score;

    @Override
    public String toString() {
        return String.format("%s->%d (%s)", text, score, lettersToString(letters));
    }

    private static String lettersToString(final List<Letter> letters) {
        Objects.requireNonNull(letters, "List should not be null");

        return letters.stream()
                .map(Letter::toString)
                .collect(Collectors.joining(", "));
    }
}
