package edu.upf.parser;

import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SimplifiedTweet {

  // ...

  // All classes use the same instance
  private static JsonParser parser = new JsonParser();

  private final long tweetId;			  // the id of the tweet ('id')
  private final String text;  		      // the content of the tweet ('text')
  private final long userId;			  // the user id ('user->id')
  private final String userName;		  // the user name ('user'->'name')
  private final String language;          // the language of a tweet ('lang')
  private final long timestampMs;		  // seconduserIds from epoch ('timestamp_ms')

  public SimplifiedTweet(long tweetId, String text, long userId, String userName,
                         String language, long timestampMs) {

    this.tweetId = tweetId;
    this.text = text;
    this.userId = userId;
    this.userName = userName;
    this.language = language;
    this.timestampMs = timestampMs;

  }
  
  public long getTweetId() {
    return tweetId;
  }

  public String getText() {
    return text;
  }

  public long getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getLanguage() {
    return language;
  }

  public long getTimestampMs() {
    return timestampMs;
  }
  /**
   * Returns a {@link SimplifiedTweet} from a JSON String.
   * If parsing fails, for any reason, return an {@link Optional#empty()}
   *
   * @param jsonStr
   * @return an {@link Optional} of a {@link SimplifiedTweet}
   */
  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
      try {

          JsonObject jsonObject = parser.parse(jsonStr).getAsJsonObject();
          long tweetId = jsonObject.get("id").getAsLong();
          String text = jsonObject.get("text").getAsString();
          JsonObject user = jsonObject.get("user").getAsJsonObject();
          long userId = user.get("id").getAsLong();
          String userName = user.get("name").getAsString();
          String language = jsonObject.get("lang").getAsString();
          long timestampMs = jsonObject.get("timestamp_ms").getAsLong();

          return Optional.of(new SimplifiedTweet(tweetId, text, userId, userName, language, timestampMs));
      
        } catch (Exception e) {
          return Optional.empty();
      }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
