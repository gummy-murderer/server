package com.server.gummymurderer.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class GCSService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {

        // GCS 인증 및 서비스 초기화
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();

        // 업로드할 파일의 Blob 정보 설정
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, Objects.requireNonNull(file.getOriginalFilename()))
                .setContentType(file.getContentType()).build();

        // 파일 업로드
        Blob blob = storage.create(blobInfo, file.getInputStream());

        // 업로드된 파일의 GCS URL 반환
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());
    }
}