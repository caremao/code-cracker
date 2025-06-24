package com.caremao.codebreaker.runner;

import com.caremao.codebreaker.core.model.Word;
import com.caremao.codebreaker.core.processing.WordProcessor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.*;

@Component
@CommandLine.Command(name = "code-cracker-int",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "CLI de Code Cracker")
public class CliMainRunner implements Runnable {

    private final WordProcessor wordProcessor;


    private final Scanner scanner = new Scanner(System.in);
    private final List<String> inputWords = new ArrayList<>();

    public CliMainRunner(WordProcessor wordProcessor) {
        this.wordProcessor = wordProcessor;
    }

    @Override
    public void run() {
        while (true) {
            collectWords();
            processLoop();
            printLine("Press 'Enter' to continue, type 'exit' to exit.");
            String line = scanner.nextLine().trim();
            if ("exit".equals(line)) {
                break;
            }
        }
    }

    private void collectWords() {
        printLine("Enter words. '!' to finish:");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("!")) break;

            Arrays.stream(line.split("\\s+"))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(w -> !w.isEmpty())
                    .forEach(inputWords::add);
        }
    }

    private void confirmOrCorrectWords() {
        while (true) {
            printLine("Words:");
            for (int i = 0; i < inputWords.size(); i++) {
                printFormat("%d. %s%n", i + 1, inputWords.get(i));
            }

            printLine("Input a number to correct that line, 0 to add a line, ENTER to confirm list:");
            String choice = scanner.nextLine().trim();

            if (choice.isEmpty()) {
                break;
            }

            try {
                int opt = Integer.parseInt(choice);
                if (opt == 0) {
                    print("Enter new word: ");
                    String newWord = scanner.nextLine().trim().toUpperCase();
                    if (!newWord.isEmpty()) inputWords.add(newWord);
                } else if (opt >= 1 && opt <= inputWords.size()) {
                    printFormat("Replace %s: with", inputWords.get(opt));
                    String newWord = scanner.nextLine().trim().toUpperCase();
                    if (!newWord.isEmpty()) inputWords.set(opt - 1, newWord);
                } else {
                    printLine("Index invalid.");
                }
            } catch (NumberFormatException e) {
                printLine("Command not recognized");
            }
        }
    }

    private void processLoop() {
        List<Word> currentList = wordProcessor.processWords(inputWords);
        if (Objects.isNull(currentList)) {
            printLine("Error the list could not be processed...");
        }

        while (Objects.nonNull(currentList) && currentList.size() > 1) {
            printFormat("%d words left. Most probable words (Score):\n", currentList.size());
            for (int index = 0; index < currentList.size(); index++) {
                Word word = currentList.get(index);
                printFormat("%d. %s (%d)%n", index + 1, word.getText(), word.getScore());
            }

            print("Select from the list: ");
            int selected = Integer.parseInt(scanner.nextLine().trim()) - 1;
            Word selectedWord = currentList.get(selected);

            print("Matches: ");
            try {
                int matches = Integer.parseInt(scanner.nextLine().trim());
                currentList = wordProcessor.filterByFeedback(currentList, selectedWord, matches);
            } catch (NumberFormatException e) {
                printLine("Command not recognized. Provide the amount of matches.");
            }

            printLine("Sorting list again...");
        }
        printFormat("The password is %s", currentList.get(0).getText());
    }

    private static void print(String s) {
        System.out.print(s);
    }

    private static void printLine(String x) {
        System.out.println(x);
    }

    private void printFormat(String format, Object... args) {
        System.out.printf(format, args);
    }
}
