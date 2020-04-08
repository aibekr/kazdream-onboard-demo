package kz.kazdream.demo.onboard.db.repository;

import kz.kazdream.demo.onboard.db.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
