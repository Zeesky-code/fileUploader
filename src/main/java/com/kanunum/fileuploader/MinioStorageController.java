package com.kanunum.fileuploader;
import com.kanunum.fileuploader.MinioAdapter;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MinioStorageController{
    @Autowired
    MinioAdapter minioAdapter;

    @GetMapping(path="/buckets")
    public List<Bucket> listBuckets(){
        return minioAdapter.getAllBuckets();
    }
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody ResponseEntity<String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        minioAdapter.uploadFile(files.getOriginalFilename(), files.getBytes());
        return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = minioAdapter.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }
}