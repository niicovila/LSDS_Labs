package edu.upf.filter;

import edu.upf.parser.SimplifiedTweet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Optional;

public class FileLanguageFilter implements LanguageFilter {
  final String inputFile;
  final String outputFile;
  private int line_count;

  public FileLanguageFilter(String inputFile, String outputFile) {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  public int Get_Line_Count(){
    return line_count;
  }

  @Override
  public void filterLanguage(String language) throws Exception {
    // Use try-with-resources to automatically close the file after reading
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
         PrintWriter pw = new PrintWriter(new FileWriter(outputFile, true))) {
        String line;
        line_count = 0;
        while ((line = br.readLine()) != null) {
            Optional<SimplifiedTweet> st = SimplifiedTweet.fromJson(line);
            if (st.isPresent() && st.get().getLanguage().equals(language)) {
                // Write the filtered tweet to the output file
                pw.println(line);
                line_count++;
            }
        }
        System.out.println("File: " + inputFile + "written to: " + outputFile);
    } catch (Exception e) {
        throw new Exception("Error processing file", e);
    }
}
}
