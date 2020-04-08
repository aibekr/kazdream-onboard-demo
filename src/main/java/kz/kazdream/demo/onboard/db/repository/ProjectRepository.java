package kz.kazdream.demo.onboard.db.repository;

import kz.kazdream.demo.onboard.db.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
