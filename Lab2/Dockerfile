FROM apache/spark

# copy the jar into the container
COPY /target/spark-test-1.0-SNAPSHOT.jar job.jar
COPY shakespeare.txt shakespeare.txt
COPY /Data/ /Data/

# submit (run) the spark job
CMD ["/opt/spark/bin/spark-submit", "--class", "edu.upf.spark.BiGramsApp", "job.jar", "es", " ./out1/", "/Data/"]
# CMD ["/opt/spark/bin/spark-submit", "--class", "edu.upf.TwitterLanguageFilterApp", "job.jar", "es", " ./out1/", "/Data/"]
# CMD ["/opt/spark/bin/spark-submit", "--class", "edu.upf.model.MostRetweetedApp", "job.jar", " ./out1/", "/Data/"]