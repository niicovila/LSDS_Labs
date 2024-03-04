package edu.upf.model;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class MostRetweetedApp {
    private static final int NUM_TOP_USERS = 10;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: MostRetweetedApp <input-path> <output-path>");
            System.exit(1);
        }
        String outputPath = args[0];
        String inputPath = args[1];

        SparkConf sparkConf = new SparkConf().setAppName("Most Retweeted");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        
        JavaRDD<String> tweetsRDD = sparkContext.textFile(inputPath + "/*.json");

        JavaRDD<ExtendedSimplifiedTweet> parsedTweetsRDD = tweetsRDD.flatMap(line -> {
            Optional<ExtendedSimplifiedTweet> optTweet = ExtendedSimplifiedTweet.fromJson(line);
            return optTweet.isPresent() ? Collections.singletonList(optTweet.get()).iterator() : Collections.emptyIterator();
        });
        
        JavaRDD<ExtendedSimplifiedTweet> retweetsRDD = parsedTweetsRDD.filter(tweet -> tweet.isRetweet());

        JavaPairRDD<Long, Integer> retweetedUserCountsRDD = retweetsRDD
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), 1))
                .reduceByKey(Integer::sum);

        List<Tuple2<Integer, Long>> sortedUserCounts = retweetedUserCountsRDD
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .take(NUM_TOP_USERS);

        List<Long> mostRetweetedUsers = sortedUserCounts.stream()
                .map(Tuple2::_2)
                .collect(Collectors.toList());

        JavaPairRDD<Long, ExtendedSimplifiedTweet> userTweetsRDD = parsedTweetsRDD
                .filter(tweet -> tweet.isRetweet() && mostRetweetedUsers.contains(tweet.getRetweetedUserId()))
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), tweet));

        JavaPairRDD<Long, Iterable<ExtendedSimplifiedTweet>> groupedTweetsRDD = userTweetsRDD.groupByKey();

        Map<Long, ExtendedSimplifiedTweet> mostRetweetedTweets = groupedTweetsRDD
                .mapValues(tweets -> {
                    Map<ExtendedSimplifiedTweet, Integer> tweetCounts = new HashMap<>();
                    for (ExtendedSimplifiedTweet tweet : tweets) {
                        int count = countTweetFrequency(tweets, tweet);
                        tweetCounts.put(tweet, count);
                    }
                    return tweetCounts.entrySet().stream()
                            .max(Comparator.comparingInt(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .orElse(null);
                })
                .collectAsMap();

        try (PrintWriter writer = new PrintWriter(outputPath)) {
            writer.println("Top " + NUM_TOP_USERS + " retweeted tweets of the most retweeted users:");
            int j = 0;
            for (long userId : mostRetweetedUsers) {
                ExtendedSimplifiedTweet tweet = mostRetweetedTweets.get(userId);
                j+=1;
                writer.println(j + ". User " + userId + ": " + tweet.getText() );

            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("Top " + NUM_TOP_USERS + " retweeted tweets of the most retweeted users:");
        int j = 0;
        for (long userId : mostRetweetedUsers) {
            ExtendedSimplifiedTweet tweet = mostRetweetedTweets.get(userId);
            j+=1;
            System.out.println(j + ". User " + userId + ": " + tweet.getText());
        }
    
        sparkContext.stop();
    }

    private static int countTweetFrequency(Iterable<ExtendedSimplifiedTweet> tweets, ExtendedSimplifiedTweet tweet) {
        int count = 0;
        for (ExtendedSimplifiedTweet t : tweets) {
            if (t.equals(tweet)) {
                count++;
            }
        }
        return count;
    }
}
