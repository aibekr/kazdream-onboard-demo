package kz.kazdream.demo.onboard.db.service.impl;

import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.repository.ProjectRepository;
import kz.kazdream.demo.onboard.db.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;

    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public Project save(Project project) {
        return repository.save(project);
    }

    @Override
    public List<Project> saveAll(List<Project> projects) {
        return repository.saveAll(projects);
    }

    @Override
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public Project findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Project project) {
        repository.delete(project);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Project edit(Project project) {
        return repository.save(project);
    }
}
