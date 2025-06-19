package com.caremao.codebreaker.core.processing;

import com.caremao.codebreaker.core.CoreConfiguration;
import com.caremao.codebreaker.core.model.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CoreConfiguration.class)
class WordProcessorTest {

    @Autowired
    WordProcessor processor;

    @Test
    void processWordsShouldProcessAndReturnOrderedWords() {
        // Arrange
        List<String> words = List.of("SAID",
                "SEEN",
                "PENS",
                "DOES",
                "CHIP",
                "SEEM",
                "SLUM",
                "FEED",
                "HELP",
                "TEAM",
                "BEGS",
                "MENU",
                "READ",
                "REST",
                "TRIP",
                "HERE",
                "FLEE"
        );

        // Act
        List<Word> processedWords = processor.processWords(words);

        // Assert
        printWords(processedWords);

        assertThat(processedWords)
                .describedAs("Sort words by its weight")
                .extracting(Word::getText, Word::getScore)
                .containsExactly(
                        tuple("SEEM", 23L),
                        tuple("SEEN", 21L),
                        tuple("FEED", 21L),
                        tuple("TEAM", 18L),
                        tuple("READ", 18L),
                        tuple("PENS", 17L),
                        tuple("HELP", 17L),
                        tuple("BEGS", 16L),
                        tuple("HERE", 16L),
                        tuple("MENU", 15L),
                        tuple("REST", 15L),
                        tuple("SAID", 11L),
                        tuple("FLEE", 11L),
                        tuple("DOES", 10L),
                        tuple("SLUM", 10L),
                        tuple("TRIP", 9L),
                        tuple("CHIP", 8L)
                );

    }

    @Test
    void filterByFeedbackShouldFilterUnwantedWords() {
        // Arrange
        List<String> words = List.of("SAID",
                "SEEN",
                "PENS",
                "DOES",
                "CHIP",
                "SEEM",
                "SLUM",
                "FEED",
                "HELP",
                "TEAM",
                "BEGS",
                "MENU",
                "READ",
                "REST",
                "TRIP",
                "HERE",
                "FLEE"
        );

        // Act
        List<Word> processedWords = processor.processWords(words);
        List<Word> filteredWords = processor.filterByFeedback(processedWords, "SEEM", 0);

        // Assert
        printWords(filteredWords);

        assertThat(filteredWords)
                .describedAs("Sort words by its weight")
                .extracting(Word::getText, Word::getScore)
                .containsExactly(
                        tuple("TRIP", 6L),
                        tuple("CHIP", 6L)
                );

    }

    private static void printWords(List<Word> processedWords) {
        System.out.println("List of words\n" + processedWords.stream()
                .map(Word::toString)
                .collect(Collectors.joining("\n")));
    }
}