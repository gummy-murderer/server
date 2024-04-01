package com.server.gummymurderer.controller;

import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.S3FileTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3FileTestController {

    private final S3FileTestService s3FileTestService;

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> uploadFile(@RequestPart("file")MultipartFile file) throws IOException {
        String url = s3FileTestService.uploadFile(file);
        return Response.success(url);
    }
}
