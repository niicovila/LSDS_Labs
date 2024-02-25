package edu.upf;

import edu.upf.filter.FileLanguageFilter;
import edu.upf.uploader.S3Uploader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class TwitterFilter {
    public static void main( String[] args ) throws Exception {
        if (args.length < 4) {
          System.out.println("Invalid arguments");
          System.exit(1);
        }
        int totalTweetsCount = 0;
        List<String> argumentsList = Arrays.asList(args);
        String targetLanguage = argumentsList.get(0);
        String outputFilePath = argumentsList.get(1);
        String s3BucketName = argumentsList.get(2);

        clearFile(outputFilePath);

        long startTime = System.nanoTime();
        for(String inputFilePath: argumentsList.subList(3, argumentsList.size())) {
          System.out.println("Processing: " + inputFilePath);
          if (!new File(inputFilePath).exists()) {
              System.out.println("File does not exist: " + inputFilePath);
              continue;
          }
          FileLanguageFilter languageFilter = new FileLanguageFilter(inputFilePath, outputFilePath);
          languageFilter.filterLanguage(targetLanguage);
          totalTweetsCount += languageFilter.Get_Line_Count();
        }
        long endTime = System.nanoTime();
        long totalTimeForFileBuilding = endTime - startTime;
    
        startTime = System.nanoTime();
        List<String> filesToUpload = Arrays.asList(outputFilePath);
        String s3Prefix = targetLanguage;
        String awsProfileName ="default";
        S3Uploader s3Uploader = new S3Uploader(s3BucketName, s3Prefix, awsProfileName);
        s3Uploader.upload(filesToUpload);
        endTime = System.nanoTime();
        long totalTimeForUpload = endTime - startTime;
    
        System.out.println("Building time of " + outputFilePath + ": " + totalTimeForFileBuilding/ 1000000000.0 + " seconds" );
        System.out.println("Uploading time to S3: " + totalTimeForUpload/ 1000000000.0 + " seconds");
        System.out.println("Number of tweets: " + totalTweetsCount);
        System.exit(0);
    }

    public static void clearFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Error clearing file content: " + filePath);
        }
    }
}
