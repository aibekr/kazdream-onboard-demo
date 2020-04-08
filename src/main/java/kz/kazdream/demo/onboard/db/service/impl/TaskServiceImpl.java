package kz.kazdream.demo.onboard.db.service.impl;

import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.repository.TaskRepository;
import kz.kazdream.demo.onboard.db.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public List<Task> saveAll(List<Task> tasks) {
        return repository.saveAll(tasks);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Task findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Task task) {
        repository.delete(task);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Task edit(Task task) {
        return repository.save(task);
    }

    @Override
    public List<Task> findAllById(List<Integer> taskIds) {
        return repository.findAllById(taskIds);
    }
}
