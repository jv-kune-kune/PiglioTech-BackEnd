package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Region;
import org.kunekune.PiglioTech.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUserByUid(String uid);
    List<User> getUsersByRegion(Region region);
    List<User> getUsersByRegionExclude(Region region, String exclude);
}
