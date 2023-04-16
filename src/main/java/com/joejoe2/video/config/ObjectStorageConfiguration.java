package com.joejoe2.video.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ObjectStorageConfiguration {
  @Value("${minio.access-key}")
  private String accessKey;

  @Value("${minio.secret-key}")
  private String secretKey;

  @Value("${minio.url}")
  private String url;

  @Value("${minio.bucket.store}")
  private String storeBucket;

  @Value("${minio.bucket.stream}")
  private String streamBucket;

  private static final Logger logger = LoggerFactory.getLogger(ObjectStorageConfiguration.class);

  @Bean
  public MinioClient minioClient() throws Exception {
    MinioClient minioClient =
        MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    initBucket(minioClient, storeBucket);
    initBucket(minioClient, streamBucket);
    return minioClient;
  }

  private void initBucket(MinioClient minioClient, String bucket) throws Exception {
    boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    if (!exists) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
      logger.info("successfully create bucket {} !", bucket);
    }
  }
}
