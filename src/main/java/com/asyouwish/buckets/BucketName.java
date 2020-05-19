package com.asyouwish.buckets;

public enum BucketName {

    PROFILE_IMAGE("myasyouwish-spring-boot-reactjs-bucket");
    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String value() {
        return bucketName;
    }
}
