package com.joejoe2.video.service.storage;

import com.joejoe2.video.config.ObjectStorageConfiguration;
import io.minio.messages.Item;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioObjectStorageServiceImpl implements ObjectStorageService {
  @Autowired MinioService minioService;
  @Autowired ObjectStorageConfiguration objectStorageConfiguration;

  @Override
  public void upload(MultipartFile file, String objectName) throws Exception {
    minioService.putObject(file, objectName);
  }

  @Override
  public void createFolder(String folderName) throws Exception {
    minioService.putFolder(folderName);
  }

  @Override
  public List<String> listFiles(String bucket, String folderName) throws Exception {
    List<String> files = new ArrayList<>();
    return minioService.listFiles(bucket, folderName).stream().map(Item::objectName).toList();
  }

  @Override
  public InputStream readFile(String bucket, String objectName) throws Exception {
    return minioService.getObject(bucket, objectName);
  }

  @Override
  public void delete(String objectName) throws Exception {
    minioService.removeObject(objectName);
  }
}
