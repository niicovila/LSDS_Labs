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

  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
    Optional<SimplifiedTweet> st = Optional.empty();
    Gson gson = new Gson();
    JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);

    if (isValidJsonObject(obj)) {
        Long userId = getJsonElementValue(obj, "user.id", 0L);
        String userName = getJsonElementValue(obj, "user.name", "");
        Long tweetId = getJsonElementValue(obj, "id", 0L);
        String text = getJsonElementValue(obj, "text", "");
        String language = getJsonElementValue(obj, "lang", "");
        Long timestampMs = getJsonElementValue(obj, "timestamp_ms", 0L);

        st = Optional.of(new SimplifiedTweet(tweetId, text, userId, userName, language, timestampMs));
    }

    return st;
}

private static boolean isValidJsonObject(JsonObject obj) {
    return obj != null &&
            obj.has("id") &&
            obj.has("text") &&
            obj.has("lang") &&
            obj.has("timestamp_ms") &&
            obj.has("user") &&
            obj.get("user").isJsonObject() &&
            obj.getAsJsonObject("user").has("id") &&
            obj.getAsJsonObject("user").has("name");
}

private static <T> T getJsonElementValue(JsonObject obj, String key, T defaultValue) {
    JsonElement element = obj;
    String[] keys = key.split("\\.");
    for (String k : keys) {
        if (element.isJsonObject() && element.getAsJsonObject().has(k)) {
            element = element.getAsJsonObject().get(k);
        } else {
            return defaultValue;
        }
    }

    if (element.isJsonNull()) {
        return defaultValue;
    }

    try {
        return (T) element.getAsObject();
    } catch (ClassCastException e) {
        return defaultValue;
    }
}


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
