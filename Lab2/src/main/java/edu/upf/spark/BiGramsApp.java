package edu.upf.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import edu.upf.model.ExtendedSimplifiedTweet;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
public class BiGramsApp {

    public static void main(String[] args) throws Exception {

        String language = args[0];
        String output = args[1];
        String input = args[2];

        SparkConf conf = new SparkConf().setAppName("BiGramsApp");
        
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(input);
        JavaRDD<ExtendedSimplifiedTweet> tweets = lines.map(ExtendedSimplifiedTweet::fromJson)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .filter(tweet -> tweet.getLanguage().equals(language));
        
        // Split tweets into words and create bigrams
        JavaRDD<String> wordsRDD = tweets.flatMap(tweet -> Arrays.asList(tweet.getText().split("\\s+")).iterator());
        JavaPairRDD<Bigram, Integer> bigramsRDD = wordsRDD.filter(word -> !word.isEmpty())
                .mapToPair(word -> new Tuple2<>(new Bigram(word.trim().toLowerCase(), ""), 1))
                .union(wordsRDD.filter(word -> !word.isEmpty())
                        .mapToPair(word -> new Tuple2<>(new Bigram("", word.trim().toLowerCase()), 1)))
                .reduceByKey(Integer::sum);

        // Get the top 10 most frequent bigrams
        List<Tuple2<Integer,Bigram>> top10Bigrams = bigramsRDD.mapToPair(pair -> new Tuple2<>(pair._2, pair._1))
                .sortByKey(false)
                .take(20); //take 20 to have the count of the 10 first pairs of bi-grams.

        // Save the result to a text file
        sc.parallelize(top10Bigrams)
                .map(pair -> pair._2() + ": " + pair._1())
                .saveAsTextFile(output);

        // Stop the Spark Context

        sc.stop();
        sc.close();
    }
      
    private static class Bigram implements Serializable{

        private final String word1;
        private final String word2;

        public Bigram(String word1, String word2) {
            this.word1 = word1;
            this.word2 = word2;
        }

        public String getWord1() {
            return word1;
        }

        public String getWord2() {
            return word2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Bigram) {
                Bigram other = (Bigram) obj;
                return this.word1.equals(other.word1) && this.word2.equals(other.word2);
            }
            return false;
        }
    
        @Override
        public int hashCode() {
            return (word1 + " " + word2).hashCode();
        }
    }

}