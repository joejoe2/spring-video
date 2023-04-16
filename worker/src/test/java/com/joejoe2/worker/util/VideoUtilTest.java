package com.joejoe2.worker.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

class VideoUtilTest {

  @Test
  void transcodeToM3u8() throws Exception {
    File tempFolder =
        Files.createTempDirectory("tmp-" + Instant.now().getEpochSecond() + "-" + UUID.randomUUID())
            .toFile();
    VideoUtil.transcodeToM3u8(new File("test.mp4"), tempFolder);
    FileSystemUtils.deleteRecursively(tempFolder);
  }
}
