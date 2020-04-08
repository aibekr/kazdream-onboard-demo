package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.repository.TaskRepository;
import kz.kazdream.demo.onboard.db.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
class TaskServiceTest {
    @TestConfiguration
    static class TaskServiceImplTestContextConfiguration {

        @Bean
        public TaskService taskService(TaskRepository repository) {
            return new TaskServiceImpl(repository);
        }
    }

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository repository;

    @Test
    void save_task() {
        Task expected = taskCreator("Task1");
        expected.setId(1);

        when(repository.save(expected)).thenReturn(expected);

        Task actual = taskService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_all_tasks() {
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(taskCreator("Task1"));
        tasks.add(taskCreator("Task2"));
        tasks.add(taskCreator("Task3"));
        tasks.add(taskCreator("Task4"));
        tasks.add(taskCreator("Task5"));
        tasks.add(taskCreator("Task6"));
        tasks.add(taskCreator("Task7"));

        when(repository.saveAll(tasks)).thenReturn(tasks);

        List<Task> actual = taskService.saveAll(tasks);

        assertEquals(tasks.size(), actual.size());
        assertEquals(tasks, actual);
    }

    @Test
    void find_all_tasks() {
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(taskCreator("Task1"));
        tasks.add(taskCreator("Task2"));
        tasks.add(taskCreator("Task3"));
        tasks.add(taskCreator("Task4"));
        tasks.add(taskCreator("Task5"));
        tasks.add(taskCreator("Task6"));
        tasks.add(taskCreator("Task7"));

        when(repository.findAll()).thenReturn(tasks);

        List<Task> actual = taskService.findAll();

        assertEquals(tasks.size(), actual.size());
        assertEquals(tasks, actual);
    }

    @Test
    void find_by_id_task() {
        Task task = taskCreator("Task");
        task.setId(1);
        Optional<Task> expected = Optional.of(task);
        when(repository.findById(1)).thenReturn(expected);

        Task actual = taskService.findById(1);

        assertEquals(task, actual);
    }

    @Test
    void delete_task() {
        Task task = taskCreator("Removed");
        task.setId(1);

        taskService.delete(task);

        verify(repository, times(1)).delete(task);
    }

    @Test
    void delete_all_tasks() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        taskService.deleteAll();

        int size = taskService.findAll().size();
        assertEquals(0, size);
    }

    @Test
    void edit_task() {
        Task task = taskCreator("Task");
        task.setId(1);
        Task editedTask = taskCreator("Edited");
        when(repository.save(task)).thenReturn(editedTask);

        Task actual = taskService.edit(task);

        assertEquals(editedTask, actual);
    }

    private Task taskCreator(String name) {
        Task pr = new Task();
        pr.setTaskName(name);
        pr.setAssignee(1);
        return pr;
    }
}
