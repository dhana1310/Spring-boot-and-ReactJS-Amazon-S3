package com.asyouwish.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileStore {

    private final AmazonS3 amazonS3;

    public PutObjectResult save(String path, String filename, InputStream inputStream, Optional<Map<String, String>> optionalMetaData) {

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetaData.ifPresent(metadataMap -> {
            if (!metadataMap.isEmpty()) {
                metadataMap.forEach(metadata::addUserMetadata);
            }
        });

        try {
            return amazonS3.putObject(path, filename, inputStream, metadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to S3 - ", e);
        }
    }

    public byte[] download(String userProfileId, String path, String filename) {
        try {
            return amazonS3.getObject(path, filename).getObjectContent().readAllBytes();
        } catch (AmazonServiceException | IOException e) {
            // in case the AWS account is deactivated or not working, read the static images
            try {
                Path filePath = Paths.get(getClass().getResource("/static/" + userProfileId + ".jpeg").toURI());
                return Files.readAllBytes(filePath);
            } catch (IOException | URISyntaxException ee) {
                throw new IllegalStateException(e);
            }
        }
    }
}
