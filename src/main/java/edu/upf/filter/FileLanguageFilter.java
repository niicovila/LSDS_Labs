package edu.upf.filter;


import java.io.*;
import java.util.Optional;

import edu.upf.parser.SimplifiedTweet;

public class FileLanguageFilter implements LanguageFilter {
    private String inputFile;
    private String outputFile;

    public FileLanguageFilter(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void filterLanguage(String language) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
                
                StringBuilder stringBuilder = new StringBuilder(); // Create a StringBuilder to store the lines
                
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line); // Append each line to the StringBuilder
                }
                
                String content = stringBuilder.toString(); // Convert the StringBuilder to a single string
                
                Optional<SimplifiedTweet> tweet = parseLineToTweet(content); // Parse the content as a SimplifiedTweet

                if (tweet.isPresent() && tweet.get().getLanguage().equals(language)) {
                    writer.write(content);
                    writer.newLine();
                }
                
            } catch (IOException e) {
            throw new Exception("Error processing file", e);
        }
    }

    private Optional<SimplifiedTweet> parseLineToTweet(String line) {
        return null;
        // Implement this method based on your SimplifiedTweet structure
        // Return an Optional<SimplifiedTweet>
    }
}
