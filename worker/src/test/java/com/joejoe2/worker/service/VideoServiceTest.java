package com.joejoe2.worker.service;

import static org.junit.jupiter.api.Assertions.*;

import com.joejoe2.worker.TestContext;
import com.joejoe2.worker.config.MinioConfiguration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(TestContext.class)
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class VideoServiceTest {
  @Autowired VideoService videoService;
  @Autowired MinioService minioService;
  @Autowired MinioConfiguration minioConfiguration;
  File file = new File("test.mp4");
  String userId = UUID.randomUUID().toString();
  String videoId = UUID.randomUUID().toString();

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void download() throws Exception {
    // prepare
    String path = "user/" + userId + "/" + file.getName();
    assertTrue(path.endsWith("mp4"));
    minioService.uploadObject(file, minioConfiguration.getStoreBucket(), path);
    // test
    videoService.download("minio.bucket.store://" + path, "download/" + videoId);
    File src = new File("download/" + videoId);
    src.deleteOnExit();
    assertTrue(src.exists());
  }

  @Test
  void convert() throws Exception {
    // prepare
    String path = "user/" + userId + "/" + file.getName();
    assertTrue(path.endsWith("mp4"));
    minioService.uploadObject(file, minioConfiguration.getStoreBucket(), path);
    videoService.download("minio.bucket.store://" + path, "download/" + videoId);
    File src = new File("download/" + videoId);
    src.deleteOnExit();
    // test
    File tempDir = Files.createTempDirectory(Path.of("tmp"), "tmp-" + src.getName()).toFile();
    videoService.convert(src, tempDir);
    assertTrue(tempDir.listFiles().length > 0);
  }

  @Test
  void upload() throws Exception {
    // prepare
    String path = "user/" + userId + "/" + file.getName();
    assertTrue(path.endsWith("mp4"));
    minioService.uploadObject(file, minioConfiguration.getStoreBucket(), path);
    videoService.download("minio.bucket.store://" + path, "download/" + videoId);
    File src = new File("download/" + videoId);
    src.deleteOnExit();
    File tempDir = Files.createTempDirectory(Path.of("tmp"), "tmp-" + src.getName()).toFile();
    videoService.convert(src, tempDir);
    // test
    videoService.upload(tempDir, "user/" + userId + "/video/" + videoId + "/");
  }
}
