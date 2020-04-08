package kz.kazdream.demo.onboard.db.entity;

import kz.kazdream.demo.onboard.db.repository.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ProjectTest {

    @Autowired
    private ProjectRepository repository;

    @Test
    void add_first_project_success() {
        Project expected = new Project();
        expected.setName("Project1");
        expected.setCreatedDate(new Date());
        expected.setTasks(new ArrayList<>());

        repository.save(expected);

        assertNotNull(expected.getId());
    }

    @Test
    void find_first_project_success() {

        Project expected = new Project();
        expected.setName("Project1");
        expected.setCreatedDate(new Date());
        expected.setTasks(new ArrayList<>());

        repository.save(expected);

        Project actual = repository.findById(expected.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void find_project_should_return_zero() {

        List<Project> projects = iteratorToList(repository.findAll().iterator());

        assertEquals(0, projects.size());
    }

    @Test
    void add_list_projects() {
        List<Project> expected = new ArrayList<>();
        expected.add(projectCreator("project1"));
        expected.add(projectCreator("project2"));
        expected.add(projectCreator("project3"));
        expected.add(projectCreator("project4"));
        expected.add(projectCreator("project5"));
        expected.add(projectCreator("project6"));
        expected.add(projectCreator("project7"));

        repository.saveAll(expected);

        List<Project> actual = iteratorToList(repository.findAll().iterator());

        assertEquals(expected, actual);
    }

    @Test
    void remove_project() {
        Project expected = projectCreator("Removed");
        Project save = repository.save(expected);

        repository.delete(save);

        Project project = repository.findById(save.getId()).orElse(null);
        Assertions.assertNull(project);
    }

    @Test
    void remove_list_projects() {
        List<Project> expected = new ArrayList<>();
        expected.add(projectCreator("project1"));
        expected.add(projectCreator("project2"));
        expected.add(projectCreator("project3"));
        expected.add(projectCreator("project4"));
        expected.add(projectCreator("project5"));
        expected.add(projectCreator("project6"));
        expected.add(projectCreator("project7"));

        repository.saveAll(expected);

        repository.deleteAll();

        int actualSize = iteratorToList(repository.findAll().iterator()).size();
        assertEquals(0, actualSize);
    }

    @Test
    void edit_project() {

        String newProjectName = "NewProjectName";
        String oldProjectName = "OldProjectName";
        Project expected = projectCreator(oldProjectName);
        repository.save(expected);

        expected.setName(newProjectName);
        repository.save(expected);

        Project actual = repository.findById(expected.getId()).orElse(null);

        assertNotNull(actual);
        assertEquals(newProjectName, actual.getName());
    }

    private List<Project> iteratorToList(Iterator<Project> iterator) {
        List<Project> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    private Project projectCreator(String name) {
        Project pr = new Project();
        pr.setName(name);
        pr.setCreatedDate(new Date());
        pr.setTasks(new ArrayList<>());

        return pr;
    }
}
