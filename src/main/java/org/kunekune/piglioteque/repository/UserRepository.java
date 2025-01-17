package org.kunekune.piglioteque.repository;

import org.kunekune.piglioteque.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.kunekune.piglioteque.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> getUsersByRegion(Region region);
}
