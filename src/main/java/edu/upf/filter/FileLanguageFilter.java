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
  private int _line_count;

  public FileLanguageFilter(String inputFile, String outputFile) {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  public int Get_Line_Count(){
    return _line_count;
  }

  @Override
  public void filterLanguage(String language) throws Exception {
    // Use try-with-resources to automatically close the file after reading
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
        String line;
        _line_count = 0;
        while ((line = br.readLine()) != null) {
          Optional<SimplifiedTweet> st = SimplifiedTweet.fromJson(line);
          if (st.isPresent() && st.get().getLanguage().equals(language)) {
            // Write the filtered tweet to the output file
            try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile, true))) {
              pw.println(line);
              _line_count++;
            } catch (Exception e) {
              throw new Exception("Error writing to output file", e);
            }
          }
        }
        System.out.println("The file: " + inputFile + " has been written to: " + outputFile);
      } catch (Exception e) {
        throw new Exception("Error reading input file", e);
      }
  }
}
