package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.repository.ProjectRepository;
import kz.kazdream.demo.onboard.db.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
class ProjectServiceTest {
    @TestConfiguration
    static class ProjectServiceImplTestContextConfiguration {

        @Bean
        public ProjectService projectService(ProjectRepository repository) {
            return new ProjectServiceImpl(repository);
        }
    }

    @MockBean
    private ProjectRepository repository;

    @Autowired
    private ProjectService projectService;


    @Test
    void save_project() {
        Project expected = projectCreator("Project1");
        expected.setId(1);

        when(repository.save(expected)).thenReturn(expected);

        Project actual = projectService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_all_projects() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(projectCreator("Project1"));
        projects.add(projectCreator("Project2"));
        projects.add(projectCreator("Project3"));
        projects.add(projectCreator("Project4"));
        projects.add(projectCreator("Project5"));
        projects.add(projectCreator("Project6"));
        projects.add(projectCreator("Project7"));

        when(repository.saveAll(projects)).thenReturn(projects);

        List<Project> actual = projectService.saveAll(projects);

        assertEquals(projects.size(), actual.size());
        assertEquals(projects, actual);
    }

    @Test
    void find_all_projects() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(projectCreator("Project1"));
        projects.add(projectCreator("Project2"));
        projects.add(projectCreator("Project3"));
        projects.add(projectCreator("Project4"));
        projects.add(projectCreator("Project5"));
        projects.add(projectCreator("Project6"));
        projects.add(projectCreator("Project7"));

        when(repository.findAll()).thenReturn(projects);

        List<Project> actual = projectService.findAll();

        assertEquals(projects.size(), actual.size());
        assertEquals(projects, actual);
    }

    @Test
    void find_by_id_project() {
        Project project = projectCreator("Project");
        project.setId(1);
        Optional<Project> expected = Optional.of(project);
        when(repository.findById(1)).thenReturn(expected);

        Project actual = projectService.findById(1);

        assertNotNull(actual);
        assertEquals(project, actual);
        assertEquals(0, actual.getTasks().size());
    }

    @Test
    void delete_project() {
        Project project = projectCreator("Removed");
        project.setId(1);

        projectService.delete(project);

        verify(repository, times(1)).delete(project);
    }

    @Test
    void delete_all_projects() {

        projectService.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    void edit_project() {
        Project project = projectCreator("Project");
        project.setId(1);
        Project editedProject = projectCreator("Edited");
        when(repository.save(project)).thenReturn(editedProject);

        Project actual = projectService.edit(project);

        assertEquals(editedProject, actual);
    }

    private Project projectCreator(String name) {
        Project pr = new Project();
        pr.setName(name);
        pr.setCreatedDate(new Date());
        pr.setTasks(new ArrayList<>());

        return pr;
    }
}
