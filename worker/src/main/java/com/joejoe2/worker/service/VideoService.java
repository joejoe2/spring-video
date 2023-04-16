package com.joejoe2.worker.service;

import com.joejoe2.worker.data.EventDto;
import java.io.File;

public interface VideoService {
  void handleSuccess(EventDto eventDto, String path);

  void handleFailure(EventDto eventDto, String message);

  void convert(File source, File workDir) throws Exception;

  File download(String path, String outputName) throws Exception;

  void upload(File workDir, String prefix) throws Exception;
}
