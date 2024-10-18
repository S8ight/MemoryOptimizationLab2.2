package edu.pro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String FILE_PATH = "src/edu/pro/txt/harry.txt";
    private static final int TOP_N_WORDS = 30;

    public static String cleanText(String content) {
        return content.replaceAll("[^A-Za-z ]", " ").toLowerCase(Locale.ROOT);
    }

    public static void main(String[] args) {

        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
            long startTime = System.currentTimeMillis();

            Map<String, Integer> wordFreq = lines
                    .map(Main::cleanText)
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toMap(
                            word -> word,
                            word -> 1,
                            Integer::sum));

            List<Map.Entry<String, Integer>> sortedWords = wordFreq.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                            .thenComparing(Map.Entry.comparingByKey()))
                    .collect(Collectors.toList());

            sortedWords.stream()
                    .limit(TOP_N_WORDS)
                    .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());
        }
    }
}
