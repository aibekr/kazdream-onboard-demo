package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.ProjectDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModelMapper.class, ProjectMapper.class})
class ProjectMapperTest {

    @Autowired
    private ProjectMapper mapper;

    @MockBean
    private TaskService service;

    @Test
    void entity_tasklist_null_to_dto() {
        Project enitity = new Project();
        enitity.setId(1);

        ProjectDto dto = mapper.toDto(enitity);

        assertEquals(enitity.getId(), dto.getId());
    }

    @Test
    void entity_to_to_dto() {
        Project expected = new Project();
        expected.setId(1);
        expected.setName("Entity");
        expected.setCreatedDate(new Date());

        Task task = new Task();
        task.setId(1);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        expected.setTasks(tasks);

        ProjectDto actual = mapper.toDto(expected);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(
                tasks.stream().map(Task::getId).mapToInt(i -> i).toArray().length,
                actual.getTaskIds().length);
        assertEquals(tasks.get(0).getId(), actual.getTaskIds()[0]);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
    }

    @Test
    void dto_if_tasklist_empty_to_entity() {
        ProjectDto expected = new ProjectDto();
        expected.setId(1);

        Project actual = mapper.toEntity(expected);

        assertNull(actual.getTasks());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void dto_to_entity() {
        ProjectDto expected = new ProjectDto();
        expected.setId(1);
        int[] taskIds = {1};
        expected.setTaskIds(taskIds);

        List<Integer> taskIdList = Arrays.stream(taskIds).boxed().collect(Collectors.toList());

        Task task = new Task();
        task.setId(1);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(service.findAllById(taskIdList)).thenReturn(tasks);

        Project actual = mapper.toEntity(expected);

        Assertions.assertNotNull(actual.getTasks());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTaskIds()[0], actual.getTasks().get(0).getId());
    }

    @Test
    void entity_list_to_dto_list() {
        List<Project> listEntity = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Project entity = new Project();
            entity.setId(i);
            listEntity.add(entity);
        }

        List<ProjectDto> userDtos = mapper.toDtoList(listEntity);

        for (int i = 1; i < 10; i++) {
            assertEquals(listEntity.get(i - 1).getId(), userDtos.get(i - 1).getId());
        }
    }

    @Test
    void dto_list_to_entity_list() {
        List<ProjectDto> dtos = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            ProjectDto entity = new ProjectDto();
            entity.setId(i);
            dtos.add(entity);
        }

        List<Project> entityList = mapper.toEntityList(dtos);

        for (int i = 1; i < 10; i++) {
            assertEquals(dtos.get(i - 1).getId(), entityList.get(i - 1).getId());
        }
    }
}
