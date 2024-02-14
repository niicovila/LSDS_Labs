package edu.upf.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SimplifiedTweet  {

  // All classes use the same instance
  private final long tweetId;			  
  private final String text;  
  private final long userId;			
  private final String userName;	
  private final String language;        
  private final long timestampMs;		


  public SimplifiedTweet(long tweetId, String text, long userId, String userName, String language, long timestampMs) {

    this.tweetId = tweetId;
    this.text = text;
    this.userId = userId;
    this.userName = userName;
    this.language = language;
    this.timestampMs = timestampMs;

  }

  /**
   * Returns a {@link SimplifiedTweet} from a JSON String.
   * If parsing fails, for any reason, return an {@link Optional#empty()}
   *
   * @param jsonStr
   * @return an {@link Optional} of a {@link SimplifiedTweet}
   */
   
  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
   
    Optional<SimplifiedTweet> st = Optional.empty();

    Gson gson = new Gson();
    JsonObject _obj = gson.fromJson(jsonStr, JsonObject.class);
    if (_obj != null){
      if(_obj.has("id") && _obj.has("text") &&
      _obj.has("lang") && _obj.has("timestamp_ms") && _obj.has("user")){
        Long userId = (long) 0;
        String userName = "";
        JsonElement userElement = _obj.get("user");
        if (userElement != null && !userElement.isJsonNull()) {
          if (userElement.isJsonObject()) {
            JsonObject userObject = userElement.getAsJsonObject();
            JsonElement idElement = userObject.get("id");
            if (idElement != null && !idElement.isJsonNull()) {
              userId = idElement.getAsLong();
              idElement = userObject.get("name");
              if (idElement != null && !idElement.isJsonNull()) {
                userName = idElement.getAsString();
                Long tweetId = _obj.get("id").getAsLong();
                String text = _obj.get("text").getAsString();
                String language = _obj.get("lang").getAsString();
                Long timestampMs = _obj.get("timestamp_ms").getAsLong();
                
                st = Optional.of(new SimplifiedTweet(tweetId,  text, userId, userName, language, timestampMs));
              }
            }
          }
        }
      }
    }
    
    return st;

  }

  /*public static SimplifiedTweet from_Json(String jsonStr) {
    SimplifiedTweet st = null;
    Gson gson = new Gson();
    JsonObject _obj = gson.fromJson(jsonStr, JsonObject.class);
    if (_obj != null){
      if(_obj.has("id") && _obj.has("text") &&
      _obj.has("lang") && _obj.has("timestamp_ms") && _obj.has("user")){
        Long userId = (long) 0;
        String userName = "";
        JsonElement userElement = _obj.get("user");
        if (userElement != null && !userElement.isJsonNull()) {
          if (userElement.isJsonObject()) {
            JsonObject userObject = userElement.getAsJsonObject();
            JsonElement idElement = userObject.get("id");
            if (idElement != null && !idElement.isJsonNull()) {
              userId = idElement.getAsLong();
              idElement = userObject.get("name");
              if (idElement != null && !idElement.isJsonNull()) {
                userName = idElement.getAsString();
                Long tweetId = _obj.get("id").getAsLong();
                String text = _obj.get("text").getAsString();
                String language = _obj.get("lang").getAsString();
                Long timestampMs = _obj.get("timestamp_ms").getAsLong();
                st = new SimplifiedTweet(tweetId,  text, userId, userName, language, timestampMs);
              }
            }
          }
        }
      }
    }
    
    return st;
  }*/


  @Override
  public String toString() {
    return "";
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
}
