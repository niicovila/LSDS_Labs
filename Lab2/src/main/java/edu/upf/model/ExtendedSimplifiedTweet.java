package edu.upf.model;

import java.io.Serializable;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ExtendedSimplifiedTweet implements Serializable {
    private final long tweetId; // the id of the tweet (’id’)
    private final String text; // the content of the tweet (’text’)
    private final long userId; // the user id (’user->id’)
    private final String userName; // the user name (’user’->’name’)
    private final long followersCount; // the number of followers (’user’->’followers_count’)
    private final String language; // the language of a tweet (’lang’)
    private final boolean isRetweeted; // is it a retweet? (the object ’retweeted_status’ exists?)
    private final Long retweetedUserId; // [if retweeted] (’retweeted_status’->’user’->’id’)
    private final Long retweetedTweetId; // [if retweeted] (’retweeted_status’->’id’)
    private final long timestampMs; // seconds from epoch (’timestamp_ms’)
    public ExtendedSimplifiedTweet(long tweetId, String text, long userId, String userName,
    long followersCount, String language, boolean isRetweeted,
    Long retweetedUserId, Long retweetedTweetId, long timestampMs) {
        // IMPLEMENT ME
        this.tweetId=tweetId;
        this.text=text;
        this.userId=userId;
        this.userName=userName;
        this.followersCount=followersCount;
        this.language=language;
        this.isRetweeted=isRetweeted;
        this.retweetedUserId=retweetedUserId;
        this.retweetedTweetId=retweetedTweetId;
        this.timestampMs=timestampMs;
    }


    /**
    * Returns a {@link ExtendedSimplifiedTweet} from a JSON String.
    * If parsing fails, for any reason, return an {@link Optional#empty()}
    *
    * @param jsonStr
    * @return an {@link Optional} of a {@link ExtendedSimplifiedTweet}
    */
    public static Optional<ExtendedSimplifiedTweet> fromJson(String jsonStr) {
        
        // IMPLEMENT ME
    
        Optional<ExtendedSimplifiedTweet> est = Optional.empty();

        try{
            Gson gson = new Gson();
            JsonObject _obj = gson.fromJson(jsonStr, JsonObject.class);
            if (_obj != null)
            {
                if(_obj.has("id") && _obj.has("text") &&
                _obj.has("lang") && _obj.has("timestamp_ms") && _obj.has("user"))
                {
                    Long userId = (long) 0;
                    String userName = "";
                    JsonElement userElement = _obj.get("user");
                    if (userElement != null && !userElement.isJsonNull()) 
                    {
                        if (userElement.isJsonObject()) 
                        {
                            JsonObject userObject = userElement.getAsJsonObject();
                            JsonElement idElement = userObject.get("id");
                            if (idElement != null && !idElement.isJsonNull()) 
                            {
                                userId = idElement.getAsLong();
                                idElement = userObject.get("name");
                                if (idElement != null && !idElement.isJsonNull()) 
                                {
                                    userName = idElement.getAsString();
                                    Long tweetId = _obj.get("id").getAsLong();
                                    String text = _obj.get("text").getAsString();
                                    String language = _obj.get("lang").getAsString();
                                    Long timestampMs = _obj.get("timestamp_ms").getAsLong();
                                    
                                    Long followersCount = userObject.get("followers_count").getAsLong();
                                    boolean isRetweeted = _obj.has("retweeted_status");
                                    Long retweetedUserId = isRetweeted ? _obj.get("retweeted_status")
                                    .getAsJsonObject().get("user").getAsJsonObject().get("id").getAsLong() : null;
                                    Long retweetedTweetId = isRetweeted ? _obj.get("retweeted_status")
                                    .getAsJsonObject().get("id").getAsLong() : null;

                                    est = Optional.of(new ExtendedSimplifiedTweet(tweetId, text, userId, userName, followersCount,
                                    language, isRetweeted, retweetedUserId, retweetedTweetId, timestampMs));
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            est = Optional.empty();
        }
        return est;
    }

    public String getLanguage(){
        return this.language;
    }

    public String getText(){
        return this.text;
    }
    
    public boolean isRetweet() {
        return this.isRetweeted;
    }

    public long getRetweetedUserId(){
        return this.retweetedTweetId;
    }
}