package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.TaskDto;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.mapper.TaskMapper;
import kz.kazdream.demo.onboard.db.service.TaskService;
import kz.kazdream.demo.onboard.rabbitmq.ProducerHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {TaskApi.class})
class TaskApiTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService service;

    @MockBean
    private ProducerHelper helper;

    @MockBean
    private TaskMapper mapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void get_task_status200() throws Exception {

        mvc.perform(get("/tasks/").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_task_should_return_list() throws Exception {
        Task task = new Task();
        task.setTaskName("Task");
        task.setId(1);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("Task");
        taskDto.setId(1);

        List<TaskDto> taskDtos = new ArrayList<>();
        taskDtos.add(taskDto);

        when(mapper.toDtoList(tasks)).thenReturn(taskDtos);

        given(service.findAll()).willReturn(tasks);

        ResultActions resultActions =
                mvc.perform(get("/tasks/").contentType(APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertEquals(
                objectMapper.writeValueAsString(taskDtos),
                resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void get_task_by_id_status_200() throws Exception {
        Task task = new Task();
        task.setTaskName("Task");
        task.setId(1);

        when(service.findById(task.getId())).thenReturn(task);

        mvc.perform(get("/tasks/" + task.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_task_by_id() throws Exception {
        Task task = new Task();
        task.setTaskName("Task");
        task.setId(1);

        when(service.findById(task.getId())).thenReturn(task);

        String actual = mvc.perform(get("/tasks/" + task.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(task), actual);
    }

    @Test
    void get_task_by_id_should_return_null() throws Exception {
        when(service.findById(Mockito.anyInt())).thenReturn(null);
        String contentAsString = mvc.perform(get("/tasks/0").contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals("", contentAsString);
    }

    @Test
    void put_task_should_return_status200() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task editedTask = new Task();
        editedTask.setId(1);
        editedTask.setTaskName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedTask);
        when(service.findById(dto.getId())).thenReturn(editedTask);
        when(service.save(editedTask)).thenReturn(editedTask);


        mvc.perform(put("/tasks/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void put_task_should_return_status201() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task editedTask = new Task();
        editedTask.setId(1);
        editedTask.setTaskName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedTask);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedTask)).thenReturn(editedTask);


        mvc.perform(put("/tasks/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void put_exist_task() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task editedTask = new Task();
        editedTask.setId(1);
        editedTask.setTaskName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedTask);
        when(service.findById(dto.getId())).thenReturn(editedTask);
        when(service.save(editedTask)).thenReturn(editedTask);

        String actual = mvc.perform(put("/tasks/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedTask), actual);
    }


    @Test
    void put_new_task() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task editedTask = new Task();
        editedTask.setId(1);
        editedTask.setTaskName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedTask);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedTask)).thenReturn(editedTask);

        String actual = mvc.perform(
                put("/tasks/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedTask), actual);
    }

    @Test
    void post_add_new_task_status201() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task entity = new Task();
        entity.setTaskName("Task");
        entity.setId(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        mvc.perform(
                post("/tasks/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());

    }


    @Test
    void post_add_new_task() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTaskName("Task");
        dto.setId(1);

        Task entity = new Task();
        entity.setTaskName("Task");
        entity.setId(1);

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        String actual = mvc.perform(
                post("/tasks/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(entity), actual);
    }

    @Test
    void delete_task_by_id_status204() throws Exception {

        Task task = new Task();
        task.setId(1);

        when(service.findById(1)).thenReturn(task);

        mvc.perform(
                delete("/tasks/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(service,times(1)).delete(task);
    }

    @Test
    void delete_task_by_id_status404() throws Exception {



        when(service.findById(1)).thenReturn(null);

        mvc.perform(
                delete("/tasks/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNotFound());

    }
}