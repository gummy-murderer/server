package com.server.gummymurderer.controller;

import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.GCSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/gcs")
@RequiredArgsConstructor
public class GCSController {

    private final GCSService gcsService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> objectUpload(@RequestPart("file") MultipartFile file) throws IOException {

        String url = gcsService.uploadFile(file);

        return Response.success(url);
    }

}
