package com.demo.admissionportal.entity.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FirebaseFileMetadata {
    private String name;
    private String bucket;
    private String generation;
    private String metageneration;
    private String contentType;
    private String timeCreated;
    private String updated;
    private String storageClass;
    private String size;
    private String md5Hash;
    private String contentEncoding;
    private String crc32c;
    private String etag;
    private String downloadTokens;
}
