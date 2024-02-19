package edu.upf;

import edu.upf.filter.FileLanguageFilter;
import edu.upf.uploader.S3Uploader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import java.io.File;

public class TwitterFilter {
    public static void main( String[] args ) throws Exception {
        int tweets_Count = 0;
        List<String> argsList = Arrays.asList(args);
        String language = argsList.get(0);
        String outputFile = argsList.get(1);
        String bucket = argsList.get(2);
        System.out.println("Language: " + language + ". Output file: " + outputFile + ". Destination bucket: " + bucket);
        clearFile(outputFile);//frist clear all content about the output file.
        long startTime = System.nanoTime();
        for(String inputFile: argsList.subList(3, argsList.size())) {
            System.out.println("Processing: " + inputFile);

            FileLanguageFilter ff = new FileLanguageFilter(inputFile, outputFile);
            ff.filterLanguage(language);
            tweets_Count += ff.Get_Line_Count();

        }
        long endTime   = System.nanoTime();
        long totalTime_build_outputFile = endTime - startTime;
        
        startTime = System.nanoTime();
        List<String> upload_files = Arrays.asList(outputFile);

        // Create an instance of S3Uploader with the specified bucket name, prefix, and credentials profile name
        String prefix = language;
        String profileName ="default";
        S3Uploader uploader_S3 = new S3Uploader(bucket, prefix, profileName);

        // Call the upload method to upload the files
        uploader_S3.upload(upload_files);

        endTime   = System.nanoTime();
        long totalTime_Upload = endTime - startTime;

        System.out.println("Building time of " + outputFile + ": " + totalTime_build_outputFile/ 1000000000.0 + " seconds" );

        System.out.println("Uploading time to S3: " + totalTime_Upload/ 1000000000.0 + " seconds");

        System.out.println("Number of tweets: " + tweets_Count);

        System.exit(0);
    }

    public static void clearFile(String fileName) {
      File file = new File(fileName);
      if (!file.exists()) {
        System.out.println("File does not exist: " + fileName);
      }
      try (PrintWriter writer = new PrintWriter(file)) {
          writer.print("");
      } catch (IOException e) {
        System.out.println("Error clearing file content: " + fileName);
      }
  }
}
