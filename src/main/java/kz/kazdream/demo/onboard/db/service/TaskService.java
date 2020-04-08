package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.Task;

import java.util.List;

public interface TaskService {
    Task save(Task expected);

    List<Task> saveAll(List<Task> tasks);

    List<Task> findAll();

    Task findById(int i);

    void delete(Task task);

    void deleteAll();

    Task edit(Task task);

    List<Task> findAllById(List<Integer> taskIds);
}
