package org.kunekune.PiglioTech.repository;

import org.springframework.data.repository.CrudRepository;
import org.kunekune.PiglioTech.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
