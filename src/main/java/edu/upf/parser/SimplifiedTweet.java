package edu.upf.parser;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SimplifiedTweet  {

  // All classes use the same instance
  private final long tweetId;			  
  private final String text;  
  private final long userId;			
  private final String userName;	
  private final String language;        
  private final long timestampMs;		
  private static final Gson gson = new Gson();


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
   
   public static Optional<SimplifiedTweet> fromJson(String jsonInput) {
    Optional<SimplifiedTweet> resultTweet = Optional.empty();
    JsonObject jsonObject = gson.fromJson(jsonInput, JsonObject.class);
    if (jsonObject != null){
        if(jsonObject.has("id") && jsonObject.has("text") &&
        jsonObject.has("lang") && jsonObject.has("timestamp_ms") && jsonObject.has("user")){
            Long userId = (long) 0;
            String userName = "";
            JsonElement userElement = jsonObject.get("user");
            if (userElement != null && !userElement.isJsonNull()) {
                if (userElement.isJsonObject()) {
                    JsonObject userObject = userElement.getAsJsonObject();
                    JsonElement idElement = userObject.get("id");
                    if (idElement != null && !idElement.isJsonNull()) {
                        userId = idElement.getAsLong();
                        idElement = userObject.get("name");
                        if (idElement != null && !idElement.isJsonNull()) {
                            userName = idElement.getAsString();
                            Long tweetId = jsonObject.get("id").getAsLong();
                            String text = jsonObject.get("text").getAsString();
                            String language = jsonObject.get("lang").getAsString();
                            Long timestampMs = jsonObject.get("timestamp_ms").getAsLong();
                            
                            resultTweet = Optional.of(new SimplifiedTweet(tweetId,  text, userId, userName, language, timestampMs));
                        }
                    }
                }
            }
        }
    }
    return resultTweet;
}

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
