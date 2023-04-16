package com.joejoe2.worker.service;

import java.io.File;

public interface MinioService {
  public void uploadObject(File file, String bucket, String objectName) throws Exception;

  void putFolder(String bucket, String folderName) throws Exception;

  public File downloadObject(String bucket, String objectName, String outputName) throws Exception;

  public void removeObject(String bucket, String objectName) throws Exception;
}
