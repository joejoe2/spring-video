package com.joejoe2.video.data;

import com.joejoe2.video.models.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetail implements UserDetails {
  private String id;
  private String username;

  public UserDetail(User user) {
    this.id = user.getId().toString();
    this.username = user.getUserName();
  }

  public UserDetail(String id, String username) {
    this.id = id;
    this.username = username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserDetail)) return false;
    UserDetail that = (UserDetail) o;
    return id.equals(that.id) && username.equals(that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  @Override
  public String toString() {
    return "UserDetail{" + "id='" + id + '\'' + ", username='" + username + '\'' + '}';
  }

  public String getId() {
    return this.id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
