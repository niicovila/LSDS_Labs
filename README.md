# LSDS_Labs
Students: u186656, u199772, u199150


 ## Benchmarking
Uploading tweets in English (en) with all the Eurovision files to bucket  lsds2024.lab1.output.u186656/en
- Total time to process tweets: 95 seconds
- Total time to upload tweets: 80 seconds
- Number of uploaded tweets: 446603

 Uploading tweets in Spanish (es) with all the Eurovision files to bucket  lsds2024.lab1.output.u186656/es
- Total time to process tweets: 100 seconds
- Total time to upload tweets: 116 seconds
- Number of uploaded tweets: 509435

 Uploading tweets in Catalan (ca) with all the Eurovision files to bucket  lsds2024.lab1.output.u186656/ca
- Total time to process tweets: 70.904933458 seconds
- Total time to upload tweets: 3.86590675 seconds
- Number of uploaded tweets: 4583

## Runtime environment: 
- CPU : Apple M2
- OS: macOS
- RAM: 8 GB

## Questions

- Does the bucket exist? What happens if it doesn’t? 
Yes, the bucket exists. If the program doesn’t find the bucket it prints an error message: Exception in thread "main" java.lang.IllegalArgumentException: Bucket does not exist.

- Did you encounter any issue when performing the calculation?
We didn’t find any issue when performing the calculations.  

## Example Execution 

```bash
mvn package
java -jar target/lab1-1.0-SNAPSHOT.jar es  /tmp/output-es.txt lsds2024.lab1.output.u186656 Data/Eurovision3.json
```

## Extensions: Unit Tests for SimplifiedTweet

The unit tests for the `SimplifiedTweet` class are located in the `SimplifiedTweetTest` class. They test the functionality of the `SimplifiedTweet` class.

### Test: testParseRealTweet

This test checks if the `fromJson` method can correctly parse a valid JSON string into a `SimplifiedTweet` object. The JSON string is read from a file named `valid-simplified-tweet.json`. The test asserts that the `fromJson` method returns a `SimplifiedTweet` object with the expected field values.

### Test: testParseInvalidJson

This test checks how the `fromJson` method handles an invalid JSON string. The JSON string is read from a file named `invalid-simplified-tweet.json` containing an invalid json. The test asserts that the `fromJson` method returns an empty `Optional` for an invalid JSON string.

### Test: testParseJsonWithMissingField

This test checks how the `fromJson` method handles a JSON string that's missing a field. The JSON string is read from a file named `simplified-tweet-with-missing-field.json`. The test asserts that the `fromJson` method returns an empty `Optional` when a necessary field is missing from the JSON string.

### Running the Tests

The tests run automatically after compiling the project with maven