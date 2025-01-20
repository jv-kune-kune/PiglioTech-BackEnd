package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
  User saveUser(User user);

  User getUserByUid(String uid);

  List<User> getAllUsers();

  List<User> getUsersByRegion(Region region);

  List<User> getUsersByRegionExclude(Region region, String exclude);

  User updateUserDetails(String uid, Map<String, Object> updates);

  boolean isValidUser(User user);

  void removeBookFromUser(String userId, String isbn);

  User addBookToUser(String id, String isbn);
}
