package kz.kazdream.demo.onboard.db.entity;

import kz.kazdream.demo.onboard.db.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TaskTest {

    @Autowired
    private TaskRepository repository;

    @Test
    void add_first_task_success() {
        Task expected = taskCreator("Task1");

        repository.save(expected);

        Assertions.assertNotNull(expected.getId());
    }

    @Test
    void find_first_task_success() {

        Task expected = taskCreator("Task1");

        repository.save(expected);

        Task actual = repository.findById(expected.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void find_task_should_return_zero() {

        List<Task> tasks = iteratorToList(repository.findAll().iterator());

        assertEquals(0, tasks.size());
    }

    @Test
    void add_list_tasks() {
        List<Task> expected = new ArrayList<>();
        expected.add(taskCreator("task1"));
        expected.add(taskCreator("task2"));
        expected.add(taskCreator("task3"));
        expected.add(taskCreator("task4"));
        expected.add(taskCreator("task5"));
        expected.add(taskCreator("task6"));
        expected.add(taskCreator("task7"));

        repository.saveAll(expected);

        List<Task> actual = iteratorToList(repository.findAll().iterator());

        assertEquals(expected, actual);
    }

    @Test
    void remove_task() {
        Task expected = taskCreator("Removed");
        Task save = repository.save(expected);

        repository.delete(save);

        Task task = repository.findById(save.getId()).orElse(null);
        Assertions.assertNull(task);
    }

    @Test
    void remove_list_tasks() {
        List<Task> expected = new ArrayList<>();
        expected.add(taskCreator("task1"));
        expected.add(taskCreator("task2"));
        expected.add(taskCreator("task3"));
        expected.add(taskCreator("task4"));
        expected.add(taskCreator("task5"));
        expected.add(taskCreator("task6"));
        expected.add(taskCreator("task7"));

        repository.saveAll(expected);

        repository.deleteAll();

        int actualSize = iteratorToList(repository.findAll().iterator()).size();
        assertEquals(0, actualSize);
    }

    @Test
    void edit_task() {

        String newTaskName = "NewTaskName";
        String oldTaskName = "OldTaskName";
        Task expected = taskCreator(oldTaskName);
        repository.save(expected);

        expected.setTaskName(newTaskName);
        repository.save(expected);

        Task actual = repository.findById(expected.getId()).orElse(null);

        assertNotNull(actual);
        assertEquals(newTaskName, actual.getTaskName());
    }

    private void assertNotNull(Task actual) {
    }

    private List<Task> iteratorToList(Iterator<Task> iterator) {
        List<Task> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    private Task taskCreator(String name) {
        Task pr = new Task();
        pr.setTaskName(name);
        pr.setAssignee(1);
        return pr;
    }

    private Project projectCreator(String name) {
        Project pr = new Project();
        pr.setName(name);
        pr.setCreatedDate(new Date());
        pr.setTasks(new ArrayList<>());

        return pr;
    }
}
