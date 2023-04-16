package com.joejoe2.video.data.video;

import com.joejoe2.video.models.video.Video;
import com.joejoe2.video.models.video.VideoStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoProfile {
  String id;
  String title;
  VideoStatus status;
  String owner;
  String creatAt;
  String eventResult;

  public VideoProfile(Video video) {
    this.id = video.getId().toString();
    this.title = video.getTitle();
    this.creatAt = video.getCreateAt().toString();
    this.status = video.getStatus();
    this.owner = video.getOwner().getId().toString();
    this.eventResult = video.getEventResult();
  }
}
