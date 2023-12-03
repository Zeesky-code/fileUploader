package com.kanunum.fileuploader;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig{
    @Value("${spring.minio.access-key}")
    String accessKey;
    @Value("${spring.minio.secret-key}")
    String secretKey;
    @Value("${spring.minio.url}")
    String minioUrl;

    @Bean
    public MinioClient generateMinioClient(){
        try{
            return MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey,secretKey)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
