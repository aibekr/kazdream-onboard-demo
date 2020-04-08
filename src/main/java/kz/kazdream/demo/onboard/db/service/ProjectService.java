package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.Project;

import java.util.List;

public interface ProjectService {
    Project save(Project project);

    List<Project> saveAll(List<Project> projects);

    List<Project> findAll();

    Project findById(int id);

    void delete(Project project);

    void deleteAll();

    Project edit(Project project);
}
