package edu.upf.model;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;
import java.io.PrintWriter;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class MostRetweetedApp {
    private static final int NUM_USERS = 10;

    public static void main(String[] args) {

        String outputPath = args[1];

        SparkConf conf = new SparkConf().setAppName("Most Retweeted");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        String inputPath = args[0];
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
                .take(NUM_USERS);

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

        System.out.println("Most retweeted tweets for the " + NUM_USERS + " most retweeted users:");
        int i = 0;
        for (long userId : mostRetweetedUsers) {
            ExtendedSimplifiedTweet tweet = mostRetweetedTweets.get(userId);
            if (tweet != null) {
                i+=1;
                System.out.println("#"+i + " User " + userId + ": " + tweet.getText() );
            }
            else {
                System.out.println("null :(" );

            }
        }

        try (PrintWriter writer = new PrintWriter(outputPath)) {
            writer.println("Most retweeted tweets for the " + NUM_USERS + " most retweeted users:");
            int j = 0;
            for (long userId : mostRetweetedUsers) {
                ExtendedSimplifiedTweet tweet = mostRetweetedTweets.get(userId);
                if (tweet != null) {
                    j+=1;
                    writer.println("#"+ j + " User " + userId + ": " + tweet.getText() );
                }
                else {
                    writer.println("null :(" );
    
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    

        sparkContext.stop();
        sparkContext.close();   
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
