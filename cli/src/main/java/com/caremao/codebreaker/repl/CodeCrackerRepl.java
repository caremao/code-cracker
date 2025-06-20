package com.caremao.codebreaker.repl;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
@CommandLine.Command(name = "code-cracker",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "CLI de Code Cracker")
public class CodeCrackerRepl implements Runnable {

    private static PrintWriter writer;
    private final List<String> inputWords = new ArrayList<>();

    @Override
    public void run() {
        try (Terminal terminal = TerminalBuilder.builder().build()) {
            LineReader reader = LineReaderBuilder.builder().build();
            writer = terminal.writer();

            writer.print("""
                    ###########################################################
                    
                    Welcome to ROBCO Industries (TM) Terminlink
                    
                    ##  AI enhanced password recovery tool.  ##################
                    
                    <Text, list of words anything to be printed>
                    
                    Type 'help' for instructions.
                    > <prompt>
                    ###########################################################
                    """);
            writer.flush();

            while (true) {
                String line;
                try {
                    line = reader.readLine("> ").trim();
                    switch (line) {
                        case "help" -> printHelp();
                        case "exit", "quit" -> {
                            writer.println("Exiting now.");
                            return;
                        }
                        default -> writer.println("Invalid command. Use 'help' for instructions.");
                    }
                } catch (UserInterruptException | EndOfFileException e) {
                    writer.println("Exiting now.");
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        if (line.isEmpty()) continue;
//
//        String[] parts = line.split("\\s+", 2);
//        String command = parts[0].toLowerCase();
//        String argument = parts.length > 1 ? parts[1] : "";
    }


    //private static void handleAdd(String argument) {
//    if (argument.isBlank()) {
//        System.out.print("Ingrese palabra: ");
//        argument = scanner.nextLine();
//    }
//    Arrays.stream(argument.trim().toUpperCase().split("\\s+"))
//            .filter(w -> !w.isEmpty())
//            .forEach(inputWords::add);
//}
//
//private static void handleList() {
//    if (inputWords.isEmpty()) {
//        System.out.println("⚠️  Lista vacía");
//        return;
//    }
//    System.out.println("📋 Palabras ingresadas:");
//    for (int i = 0; i < inputWords.size(); i++) {
//        System.out.printf("%2d. %s%n", i + 1, inputWords.get(i));
//    }
//}
//
//private static void handleSelect(String argument) {
//    try {
//        int index = Integer.parseInt(argument) - 1;
//        if (index < 0 || index >= inputWords.size()) {
//            System.out.println("❌ Índice fuera de rango");
//            return;
//        }
//        String selected = inputWords.get(index);
//        System.out.printf("Seleccionaste: %s%n", selected);
//
//        System.out.print("Ingrese número de aciertos: ");
//        int hits = Integer.parseInt(scanner.nextLine());
//
//        // TODO: llamar a lógica de coreService.filter(inputWords, selected, hits)
//        System.out.printf("(Simulado) Filtrando con %s y %d aciertos...%n", selected, hits);
//
//    } catch (NumberFormatException e) {
//        System.out.println("❌ Entrada inválida, usa un número");
//    }
//}
//
    private static void printHelp() {

        writer.print("""
                ###########################################################
                
                Welcome to ROBCO Industries (TM) Terminlink
                
                Password recovery tool.
                
                For ROBCO Industries (TM) and its subsidiaries use only!
                All connections are logged and monitored. Any unauthorized
                use will be prosecuted to the fullest extent of the law. If
                you do not agree to these conditions, disconnect now.
                
                Type 'help' for instructions.
                
                
                Available commands:
                add     - Add words to the list
                list    - Show words
                help    - Shows this help page
                exit    - Exits
                
                ###########################################################
                """);
        writer.flush();
    }
}
