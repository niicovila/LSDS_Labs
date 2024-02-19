package edu.upf;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

// TODO: 

// public class TwitterFilterTest {

//     @Test
//     public void testParseRealTweet() {
//         String tweet = "{\"id\": 123456789, \"text\": \"This is a real tweet\", \"user\": \"JohnDoe\"}";

//         TwitterFilter filter = new TwitterFilter();
//         Tweet parsedTweet = filter.parseTweet(tweet);

//         assertEquals(123456789, parsedTweet.getId());
//         assertEquals("This is a real tweet", parsedTweet.getText());
//         assertEquals("JohnDoe", parsedTweet.getUser());
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testParseInvalidJSON() {
//         String invalidJSON = "{\"id\": 123456789, \"text\": \"This is a missing closing brace\"";

//         TwitterFilter filter = new TwitterFilter();
//         filter.parseTweet(invalidJSON);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testParseMissingField() {
//         String tweet = "{\"id\": 123456789, \"user\": \"JohnDoe\"}";

//         TwitterFilter filter = new TwitterFilter();
//         filter.parseTweet(tweet);
//     }
// }

/**
 * Unit test for simple App.
 */
public class TwitterFilterTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    // Place your code here
}
