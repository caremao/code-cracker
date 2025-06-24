package com.caremao.codebreaker.runner;

import com.caremao.codebreaker.core.model.Word;
import com.caremao.codebreaker.core.processing.WordProcessor;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@CommandLine.Command(name = "code-cracker-ui", mixinStandardHelpOptions = true, description = "Lanterna CLI for Code Cracker")
public class CliLanternaRunner implements Runnable {

    private final List<String> inputWords = new ArrayList<>();
    private final WordProcessor wordProcessor;

    public CliLanternaRunner(WordProcessor wordProcessor) {
        this.wordProcessor = wordProcessor;
    }

    @Override
    public void run() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try (Screen screen = terminalFactory.createScreen()) {
            screen.startScreen();

            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
            BasicWindow window = new BasicWindow("ROBCO Industries(TM) Termlink protocol");
            window.setFixedSize(new TerminalSize(53, 22));

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            // App message label
            Label messageLabel = new Label("Welcome to the AI enhanced password recovery tool")
                    .addStyle(SGR.BOLD);

            // List of words
            Panel textListPanel = new Panel();
            textListPanel.setSize(new TerminalSize(53, 17));

            // Input words
            TextBox inputBox = new TextBox(new TerminalSize(53, 1));
            inputBox.setTextChangeListener((newText, changedByUser) -> {
                if ("\n".equals(newText)) {
                    addWordToList(inputBox.getText(), textListPanel);
                    resetInputTextBox(inputBox);
                }
            });
            TextBox wordInput = resetInputTextBox(inputBox);

            Button addButton = new Button("Add", () -> {
                String word = wordInput.getText().trim().toUpperCase();
                if (!word.isEmpty()) {
                    addWordToList(word, textListPanel);
                    wordInput.setText("");
                }
            });
//            contentPanel.addComponent(addButton);

            Button doneButton = new Button("Confirm", () -> {
                window.close();
                processLoop(screen, inputWords);
            });
//            contentPanel.addComponent(doneButton);

            contentPanel.addComponent(messageLabel);
            contentPanel.addComponent(textListPanel);
            contentPanel.addComponent(wordInput);

            window.setComponent(contentPanel);
            gui.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWordToList(String input, Panel textListPanel) {
        inputWords.add(input);
        List<Word> words = wordProcessor.processWords(inputWords);
        words.forEach(word -> {
            Label messageLabel = new Label(word.getText());

            textListPanel.addComponent(messageLabel);
        });
    }

    private TextBox resetInputTextBox(TextBox inputBox) {
        return inputBox.setText(" > ").setCaretPosition(3);
    }

    private void processLoop(Screen screen, List<String> inputWords) {
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
        List<Word> currentList = wordProcessor.processWords(inputWords);
        while (currentList != null && currentList.size() > 1) {
            BasicWindow roundWindow = new BasicWindow("Candidate Words");
            Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

            panel.addComponent(new Label(currentList.size() + " words left. Most probable:"));
            Map<Integer, Word> indexMap = new HashMap<>();
            int i = 1;
            for (Word word : currentList) {
                panel.addComponent(new Label(String.format("%d. %s (%d)", i, word.getText(), word.getScore())));
                indexMap.put(i, word);
                i++;
            }

            TextBox selectBox = new TextBox(new TerminalSize(10, 1));
            TextBox matchesBox = new TextBox(new TerminalSize(10, 1));

            panel.addComponent(new Label("Select #:"));
            panel.addComponent(selectBox);

            panel.addComponent(new Label("Matches:"));
            panel.addComponent(matchesBox);

            Button filterButton = new Button("Filter", () -> {
                try {
                    int selected = Integer.parseInt(selectBox.getText());
                    int matches = Integer.parseInt(matchesBox.getText());
                    Word selectedWord = indexMap.get(selected);
//                    currentList = wordProcessor.filterByFeedback(currentList, selectedWord, matches);
                    roundWindow.close();
                } catch (NumberFormatException e) {
                    panel.addComponent(new Label("Invalid input, try again."));
                }
            });

            panel.addComponent(filterButton);
            roundWindow.setComponent(panel);
            gui.addWindowAndWait(roundWindow);
        }

        BasicWindow end = new BasicWindow("Result");
        String result = currentList != null && !currentList.isEmpty() ? currentList.get(0).getText() : "UNKNOWN";
        end.setComponent(new Label("The password is: " + result));
        gui.addWindowAndWait(end);
    }
}
