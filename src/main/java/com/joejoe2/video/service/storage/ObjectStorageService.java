package com.joejoe2.video.service.storage;

import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
  void upload(MultipartFile file, String objectName) throws Exception;

  void createFolder(String folderName) throws Exception;

  List<String> listFiles(String bucket, String folderName) throws Exception;

  InputStream readFile(String bucket, String objectName) throws Exception;

  void delete(String objectName) throws Exception;
}
