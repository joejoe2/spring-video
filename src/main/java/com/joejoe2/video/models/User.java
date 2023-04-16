package com.joejoe2.video.models;

import com.joejoe2.video.models.video.Video;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_user")
@BatchSize(size = 64)
public class User {

  @Id
  @Column(unique = true, updatable = false, nullable = false)
  private UUID id;

  @Column(unique = true, length = 32, nullable = false)
  private String userName;

  @OneToMany(
      mappedBy = "owner",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @BatchSize(size = 32)
  List<Video> videos;

  public User(UUID id, String userName) {
    this.id = id;
    this.userName = userName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(id, user.id) && Objects.equals(userName, user.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userName);
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", userName='" + userName + '\'' + '}';
  }
}
