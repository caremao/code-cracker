package com.caremao.codebreaker.api.word;

import com.caremao.codebreaker.api.model.WordRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DataManager {

    private final WordHolder wordHolder;

    public DataManager(WordHolder wordHolder) {
        this.wordHolder = wordHolder;
    }

    public void addWord(WordRequest wordRequest) {
        if (Objects.isNull(wordRequest) || Objects.isNull(wordRequest.word())) {
            throw new IllegalArgumentException();
        }
        String word = wordRequest.word();

        // Check size
        int wordSize = wordHolder.getWordSize();
        if (wordSize != 0 && word.length() != wordSize) {
            throw new WordValidationException("Words should be of the same size.");
        }
        // Check already inserted
        if (wordHolder.getInputWords().contains(word)) {
            throw new WordValidationException("Word is already present.");
        }

        wordHolder.addWord(word);
    }

    public List<String> getWords() {
        return wordHolder.getInputWords();
    }

    public void removeWord(String word) {
        // Check already inserted
        if (!wordHolder.getInputWords().contains(word)) {
            throw new WordValidationException("Word is not present.");
        }
        wordHolder.removeWord(word);
    }

    public List<String> replaceWords(List<String> words) {
        wordHolder.reset();
        wordHolder.addWords(words);
        return wordHolder.getInputWords();
    }

    public void resetWords() {
        wordHolder.reset();
    }
}
