package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.TaskDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.service.ProjectService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModelMapper.class, TaskMapper.class})
class TaskMapperTest {

    @Autowired
    private TaskMapper mapper;

    @MockBean
    private ProjectService service;

    @Test
    void entity_tasklist_null_to_dto() {
        Task enitity = new Task();
        enitity.setId(1);

        TaskDto dto = mapper.toDto(enitity);

        assertEquals(enitity.getId(), dto.getId());
    }

    @Test
    void entity_to_to_dto() {
        Task expected = new Task();
        expected.setId(1);
        expected.setTaskName("Entity");

        Project project = new Project();
        project.setId(1);

        expected.setProject(project);

        TaskDto actual = mapper.toDto(expected);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTaskName(), actual.getTaskName());
    }

    @Test
    void dto_if_tproject_null_to_entity() {
        TaskDto expected = new TaskDto();
        expected.setId(1);

        when(service.findById(0)).thenReturn(null);

        Task actual = mapper.toEntity(expected);

        assertNull(actual.getProject());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void dto_to_entity() {
        TaskDto expected = new TaskDto();
        expected.setId(1);
        expected.setProjectId(1);

        Project project = new Project();
        project.setId(1);

        expected.setProjectId(project.getId());

        when(service.findById(project.getId())).thenReturn(project);

        Task actual = mapper.toEntity(expected);

        assertNotNull(actual.getProject());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getProjectId(), actual.getProject().getId());
    }

    @Test
    void entity_list_to_dto_list() {
        List<Task> listEntity = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Task entity = new Task();
            entity.setId(i);
            listEntity.add(entity);
        }

        List<TaskDto> userDtos = mapper.toDtoList(listEntity);

        for (int i = 1; i < 10; i++) {
            assertEquals(listEntity.get(i - 1).getId(), userDtos.get(i - 1).getId());
        }
    }

    @Test
    void dto_list_to_entity_list() {
        List<TaskDto> dtos = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            TaskDto entity = new TaskDto();
            entity.setId(i);
            dtos.add(entity);
        }

        List<Task> entityList = mapper.toEntityList(dtos);

        for (int i = 1; i < 10; i++) {
            assertEquals(dtos.get(i - 1).getId(), entityList.get(i - 1).getId());
        }
    }
}
