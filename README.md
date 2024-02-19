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
CPU : Apple M2
OS: macOS
RAM: 8 GB

We didn’t find any issue when performing the calculations.  

## Example Execution 

```bash
mvn package
java -jar target/lab1-1.0-SNAPSHOT.jar es  /tmp/output-es.txt lsds2024.lab1.output.u186656 Data/Eurovision3.json```
