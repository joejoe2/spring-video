package com.joejoe2.video.service.user.auth;

import com.joejoe2.video.data.UserDetail;
import com.joejoe2.video.models.User;
import com.joejoe2.video.repository.user.UserRepository;
import com.joejoe2.video.service.storage.ObjectStorageService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailService implements UserDetailsService {
  @Autowired UserRepository userRepository;
  @Autowired ObjectStorageService storageService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .getByUserName(username)
            .orElseThrow(() -> new UsernameNotFoundException("user does not exist !"));
    return new UserDetail(user);
  }

  @Transactional
  public void createUserIfAbsent(UserDetail userDetail) throws Exception {
    if (!userRepository.existsById(UUID.fromString(userDetail.getId()))) {
      User user = new User(UUID.fromString(userDetail.getId()), userDetail.getUsername());
      userRepository.save(user);
      storageService.createFolder("user/" + user.getId() + "/");
    }
  }
}
