package com.joejoe2.worker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoUtil {
  private static final Logger logger = LoggerFactory.getLogger(VideoUtil.class);

  public static void transcodeToM3u8(File source, File workDir) throws IOException {
    List<String> command = mp4ToHlsCommand(source.getAbsolutePath());
    Process process = new ProcessBuilder().command(command).directory(workDir).start();

    new Thread(
            () -> {
              try (BufferedReader bufferedReader =
                  new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                  logger.info(line);
                }
              } catch (IOException e) {
              }
            })
        .start();

    new Thread(
            () -> {
              try (BufferedReader bufferedReader =
                  new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                  logger.info(line);
                }
              } catch (IOException e) {
              }
            })
        .start();

    try {
      if (process.waitFor() != 0) {
        throw new RuntimeException("exit with " + process.exitValue());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> mp4ToHlsCommand(String src) {
    List<String> command = new ArrayList<>();
    command.add("ffmpeg");
    command.add("-i");
    command.add(src);
    command.add("-hls_time");
    command.add("10");
    command.add("-hls_list_size");
    command.add("0");
    // command.add("-profile:v");
    // command.add("baseline");
    // command.add("-preset");
    // command.add("ultrafast");
    command.add("-hls_segment_filename");
    command.add("%d.ts");
    command.add("index.m3u8");
    return command;
  }
}
