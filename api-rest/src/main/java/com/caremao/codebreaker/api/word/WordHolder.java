package com.caremao.codebreaker.api.word;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@SessionScope
public class WordHolder {
    @Getter
    private final List<String> inputWords = new ArrayList<>();
    @Getter
    private int wordSize;

    public void addWord(String word) {
        if (Objects.nonNull(word) && wordSize == 0) {
            wordSize = word.length();
        }
        inputWords.add(word);
    }

    public void addWords(List<String> words) {
        inputWords.addAll(words);
    }

    public void removeWord(String word) {
        inputWords.remove(word);
    }

    public void reset() {
        inputWords.clear();
        wordSize = 0;
    }
}
