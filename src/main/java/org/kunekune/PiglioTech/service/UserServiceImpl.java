package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.User;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserByUid(String uid) {
        return repository.findById(uid).orElseThrow();
    }
}
