package edu.upf;

import org.apache.spark.api.java.JavaSparkContext;
import edu.upf.model.SimplifiedTweet;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

public class TwitterLanguageFilterApp {
    public static void main(String[] args){
        String languageFilter = args[0];
        String outputDir = args[1];
        String inputPath = args[2];
        
        // Create a Spark configuration object
        SparkConf sparkConf = new SparkConf().setAppName("Twitter Language Filter");
        
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Read the input file(s) into an RDD
        JavaRDD<String> inputLinesRDD = sparkContext.textFile(inputPath);

        // Parse the input RDD to create a SimplifiedTweet RDD
        JavaRDD<SimplifiedTweet> filteredTweetRDD = inputLinesRDD
            .map(SimplifiedTweet::fromJson)
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .filter(tweet -> tweet.getLanguage().equals(languageFilter));
        
        // Extract text from SimplifiedTweet RDD
        JavaRDD<String> filteredTextRDD = filteredTweetRDD.map(SimplifiedTweet::getText);
        
        // Save filtered text RDD to output directory
        filteredTextRDD.saveAsTextFile(outputDir);
        
        // Stop Spark context
        sparkContext.stop();
    }
}