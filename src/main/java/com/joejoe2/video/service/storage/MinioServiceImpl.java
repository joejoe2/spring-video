package com.joejoe2.video.service.storage;

import com.joejoe2.video.config.ObjectStorageConfiguration;
import io.minio.*;
import io.minio.messages.Item;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioServiceImpl implements MinioService {
  @Autowired MinioClient minioClient;
  @Autowired ObjectStorageConfiguration objectStorageConfiguration;

  @Override
  public void putObject(MultipartFile file, String objectName) throws Exception {
    InputStream inputStream = file.getInputStream();
    minioClient.putObject(
        PutObjectArgs.builder()
            .bucket(objectStorageConfiguration.getStoreBucket())
            .object(objectName)
            .stream(inputStream, file.getSize(), -1)
            .contentType(file.getContentType())
            .build());
    inputStream.close();
  }

  @Override
  public void putFolder(String folderName) throws Exception {
    minioClient.putObject(
        PutObjectArgs.builder()
            .bucket(objectStorageConfiguration.getStoreBucket())
            .object(folderName)
            .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
            .build());
  }

  @Override
  public List<Item> listFiles(String bucket, String prefix) throws Exception {
    List<Item> results = new ArrayList<>();
    for (Result<Item> itemResult :
        minioClient.listObjects(
            ListObjectsArgs.builder().bucket(bucket).prefix(prefix).recursive(false).build())) {
      Item i = itemResult.get();
      if (i.isDir()) continue;
      results.add(i);
    }
    return results;
  }

  @Override
  public GetObjectResponse getObject(String bucket, String objectName) throws Exception {
    return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
  }

  @Override
  public void removeObject(String objectName) throws Exception {
    minioClient.removeObject(
        RemoveObjectArgs.builder()
            .bucket(objectStorageConfiguration.getStoreBucket())
            .object(objectName)
            .build());
  }
}
