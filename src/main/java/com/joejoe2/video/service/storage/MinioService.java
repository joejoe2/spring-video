package com.joejoe2.video.service.storage;

import io.minio.GetObjectResponse;
import io.minio.messages.Item;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
  public void putObject(MultipartFile in, String objectName) throws Exception;

  void putFolder(String folderName) throws Exception;

  List<Item> listFiles(String bucket, String prefix) throws Exception;

  public GetObjectResponse getObject(String bucket, String objectName) throws Exception;

  public void removeObject(String objectName) throws Exception;
}
