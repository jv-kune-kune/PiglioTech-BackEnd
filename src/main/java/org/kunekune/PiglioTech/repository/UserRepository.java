package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.kunekune.PiglioTech.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  List<User> getUsersByRegion(Region region);
}
