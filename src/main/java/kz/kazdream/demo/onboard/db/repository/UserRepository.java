package kz.kazdream.demo.onboard.db.repository;

import kz.kazdream.demo.onboard.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
