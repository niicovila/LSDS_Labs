package edu.upf;
import org.apache.spark.api.java.JavaSparkContext;
import edu.upf.model.SimplifiedTweet;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

public class TwitterLanguageFilterApp {
    public static void main(String[] args){
        String language = args[0];
        String outputDir = args[1];
        String input = args[2];
        // Create a Spark configuration object
        SparkConf conf = new SparkConf().setAppName("Twitter Language Filter");
        
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Read the input file(s) into an RDD
        JavaRDD<String> inputRDD = sc.textFile(input);

        // Parse the input RDD to create a SimplifiedTweet RDD
        JavaRDD<SimplifiedTweet> tweetRDD = inputRDD.map(SimplifiedTweet::fromJson)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .filter(tweet -> tweet.getLanguage().equals(language));
        
        JavaRDD<String> StringTweetRDD = tweetRDD.map(SimplifiedTweet::getText);
        
        StringTweetRDD.saveAsTextFile(outputDir);
        // Stop the Spark context
        sc.stop();
        sc.close();
    }
}
