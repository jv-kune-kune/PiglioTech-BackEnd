package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User saveUser(User user);
    User getUserByUid(String uid);
    List<User> getUsersByRegion(Region region);
    List<User> getUsersByRegionExclude(Region region, String exclude);
    User patchUserBooks (String uid, String isbn);
    User updateUserDetails(String uid, Map<String, Object> updates);    boolean isValidUser(User user);
}
