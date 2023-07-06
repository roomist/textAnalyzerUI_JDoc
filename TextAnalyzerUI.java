package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A JavaFX application that reads a file and analyzes its content, displaying word frequencies sorted by occurrence.
 */
public class TextAnalyzerUI extends Application {
    private Map<String, Integer> wordFrequencies;
    private TextArea outputTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Analyzer");

        Button openButton = new Button("Open File");
        openButton.setOnAction(e -> analyzeFile(primaryStage));

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setPrefHeight(800);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(openButton, outputTextArea);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens a file chooser dialog to select a file for analysis.
     *
     * @param primaryStage the primary stage of the JavaFX application
     */
    private void analyzeFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                wordFrequencies = countWordOccurrences(file);
                displayWordFrequencies();
            } catch (IOException e) {
                outputTextArea.setText("An error occurred while reading the file: " + e.getMessage());
            }
        }
    }

    /**
     * Counts the word occurrences in the specified file.
     *
     * @param file the file to be analyzed
     * @return a map of word frequencies, where the key is the word and the value is its occurrence count
     * @throws IOException if an error occurs while reading the file
     */
    Map<String, Integer> countWordOccurrences(File file) throws IOException {
        Map<String, Integer> wordFrequencies = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Split line into words using whitespace as delimiter

                for (String word : words) {
                    wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                }
            }
        }

        return wordFrequencies;
    }

	/**
	 * Displays the word frequencies in the output text area, sorted by occurrence count in descending order.
	 */
    private void displayWordFrequencies() {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(wordFrequencies.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : entries) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        outputTextArea.setText(sb.toString());
    }
}