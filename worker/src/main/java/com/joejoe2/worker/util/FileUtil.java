package com.joejoe2.worker.util;

import java.io.File;

public class FileUtil {
  public static void deleteDirOnExit(File dir) {
    if (!dir.isDirectory()) return;

    dir.deleteOnExit();
    File[] files = dir.listFiles();
    if (files != null) {
      for (File f : files) {
        if (f.isDirectory()) {
          deleteDirOnExit(f);
        } else {
          f.deleteOnExit();
        }
      }
    }
  }
}
