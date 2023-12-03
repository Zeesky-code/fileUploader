package com.kanunum.fileuploader;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

@Service
public class MinioAdapter {

    @Autowired
    MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    String defaultBucketName;

    @Value("${spring.minio.default-folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    public void uploadFile(String name, byte[] content) {
        File file = new File("src/main/resources/" + name);
        try (FileInputStream iofs = new FileInputStream(file);){
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(name)
                    .filename("src/main/resources/" + name)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public byte[] getFile(String key) {
        try {
            InputStream obj = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(defaultBaseFolder + "/" + key)
                            .build());

            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    public void init() {
    }
}