package com.server.gummymurderer.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

        // Signed URL 생성 옵션 설정
        URL signedUrl = storage.signUrl(
                BlobInfo.newBuilder(bucketName, blob.getName()).build(),
                // URL 유효 시간
                1, TimeUnit.HOURS,
                // signed URL을 통해 파일을 어떻게 접근할지 HTTP 메소드 정의
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                // V4 서명 방식 사용
                Storage.SignUrlOption.withV4Signature());

        // 업로드된 파일의 GCS URL 반환
        return signedUrl.toString();
    }
}