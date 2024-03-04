package edu.upf.spark;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

import edu.upf.model.ExtendedSimplifiedTweet;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BiGramsApp {

    public static void main(String[] args){
        if (args.length != 3) {
            System.err.println("Usage: BiGramsApp <language> <output-path> <input-path>");
            System.exit(1);
        }
        String language = args[0];
        String outputDir = args[1];
        String inputPath = args[2];

        // Create a SparkContext to initialize
        SparkConf conf = new SparkConf().setAppName("BiGramsExtraction");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        JavaRDD<String> tweetsRDD = sparkContext.textFile(inputPath).cache();

        JavaPairRDD<Tuple2<String, String>, Integer> biGramsRDD = tweetsRDD
                .flatMap(tweet -> Arrays.asList(tweet.split("[\n]")).iterator())
                .map(tweet -> ExtendedSimplifiedTweet.fromJson(tweet))
                .filter(tweet -> !tweet.isEmpty() && tweet.get().getLanguage().equals(language) && !tweet.get().isRetweet())
                .flatMap(tweet -> extractBiGrams(tweet.get().getText()).iterator())
                .mapToPair(biGram -> new Tuple2<>(biGram, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(tuple -> tuple.swap())
                .sortByKey(false)
                .mapToPair(tuple -> tuple.swap());

        System.out.println("Total tweets: " + biGramsRDD.count());
        biGramsRDD.saveAsTextFile(outputDir);
    }

    public static List<Tuple2<String,String>> extractBiGrams(String text) {
        List<Tuple2<String,String>> biGrams = new ArrayList<>();
        String[] words = text.trim().toLowerCase().split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {
            Tuple2<String,String> biGram = new Tuple2<>(words[i], words[i+1]);
            biGrams.add(biGram);
        }

        return biGrams;
    }
}