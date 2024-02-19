package edu.upf.uploader;

import java.io.File;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


public class S3Uploader implements Uploader {
    private final String bucketName;
    private final String prefix;    
    private final String profileName;
    private final AmazonS3 s3Client; 

    public S3Uploader(String bucketName, String prefix, String profileName) {
        this.bucketName = bucketName;
        this.prefix = prefix;
        this.profileName = profileName;
        this.s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new ProfileCredentialsProvider(this.profileName))
                        .withRegion(Regions.US_EAST_1)
                        .build();
        
    }
    @Override
    public void upload(List<String> files) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            throw new IllegalArgumentException("Bucket does not exist: " + bucketName);
        }

        for (String file : files) {
            File f = new File(file);
            String key = prefix + "/" + file;
            try {
                System.out.println("Start uploading file: " + file);
                s3Client.putObject(bucketName, key, f);
                System.out.println("The file: " + file + " was successfully uploaded.");
            } catch (AmazonServiceException e) {
                throw new RuntimeException("Error uploading file to S3: " + file, e);
            }
        }
    }
}
