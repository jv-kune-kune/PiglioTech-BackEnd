package org.kunekune.PiglioTech.service;


import org.kunekune.PiglioTech.model.User;

public interface UserService {
    User saveUser(User user);
    User getUserByUid(String uid);
}
