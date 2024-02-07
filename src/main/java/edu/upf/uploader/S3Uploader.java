package edu.upf.uploader;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;

public class S3Uploader implements Uploader {
    private final String bucketName;
    private final String prefix;
    private final String profileName;

    public S3Uploader(String bucketName, String prefix, String profileName) {
        this.bucketName = bucketName;
        this.prefix = prefix;
        this.profileName = profileName;
    }
    @Override
    public void upload(List<String> files) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider(profileName))
        .withRegion(Regions.US_EAST_1)
        .build();

        if (!s3Client.doesBucketExistV2(bucketName)) {
            throw new IllegalArgumentException("Bucket does not exist: " + bucketName);
        }

        for (String file : files) {
            File f = null;
            try {
                f = new File(file);
            } catch (Exception e) {
                System.out.println("Error opening file: " + e.getMessage());
                continue;
            }
            if(f != null){
                String key = prefix + "/" + file;
                try {
                    /*System.out.println("Start uploading file: " + file);
                    s3Client.putObject(bucketName, key, f);
                    System.out.println("The file: " + file + " was successfully uploaded.");*/
                    System.out.println("Start uploading file: " + file);
                    TransferManager tm = TransferManagerBuilder.standard().build();
                    Upload upload = tm.upload(bucketName, key, new File(file));
                    upload.addProgressListener(new ProgressListener() {
                        @Override
                        public void progressChanged(ProgressEvent progressEvent) {
                            System.out.println("Upload progress: " + String.format("%.2f", upload.getProgress().getPercentTransferred()) + "%");
                        }
                    });
                    upload.waitForCompletion();
                    System.out.println("The file: " + file + " was successfully uploaded.");

                } catch (AmazonServiceException  | InterruptedException e) {
                    throw new RuntimeException("Error uploading file to S3: " + file, e);
                }
            }
            
        }
    }
}
