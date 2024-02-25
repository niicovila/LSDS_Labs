package edu.upf;

import edu.upf.parser.SimplifiedTweet;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class SimplifiedTweetTest {

    @Test
    public void testParseRealTweet() {
        String jsonFileName = "valid-simplified-tweet.json";
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(jsonFileName)));
            Optional<SimplifiedTweet> result = SimplifiedTweet.fromJson(jsonStr);
            assertTrue(result.isPresent());
            SimplifiedTweet tweet = result.get();

            assertEquals(123L, tweet.getTweetId());
            assertEquals("a-tweet-text", tweet.getText());
            assertEquals(456L, tweet.getUserId());
            assertEquals("a-user-name", tweet.getUserName());
            assertEquals("es", tweet.getLanguage());
            assertEquals(789L, tweet.getTimestampMs());

        } catch (IOException e) {
            fail("IOException occurred while reading the file");
        }
    }
    
    @Test
    public void testParseInvalidJson() {
        String jsonFilePath = "invalid-simplified-tweet.json";

        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Optional<SimplifiedTweet> result = SimplifiedTweet.fromJson(jsonStr);
            assertFalse(result.isPresent());

        } catch (IOException e) {
            fail("IOException occurred while reading the file");
        } 
    }
    
    @Test
    public void testParseJsonWithMissingField() {
        String jsonFilePath = "simplified-tweet-with-missing-field.json";
    
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Optional<SimplifiedTweet> result = SimplifiedTweet.fromJson(jsonStr);
            assertFalse(result.isPresent());
        } catch (IOException e) {
            fail("IOException occurred while reading the file");
        }
    }
}    