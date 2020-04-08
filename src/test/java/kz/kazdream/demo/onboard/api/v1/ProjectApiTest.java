package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.ProjectDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.mapper.ProjectMapper;
import kz.kazdream.demo.onboard.db.service.ProjectService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {ProjectApi.class})
class ProjectApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectService service;

    @MockBean
    private ProducerHelper helper;

    @MockBean
    private ProjectMapper mapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void get_project_status200() throws Exception {

        mvc.perform(get("/projects/").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_project_should_return_list() throws Exception {
        Project project = new Project();
        project.setName("Project");
        project.setId(1);
        List<Project> projects = new ArrayList<>();
        projects.add(project);

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project");
        projectDto.setId(1);

        List<ProjectDto> projectDtos = new ArrayList<>();
        projectDtos.add(projectDto);

        when(mapper.toDtoList(projects)).thenReturn(projectDtos);

        given(service.findAll()).willReturn(projects);

        ResultActions resultActions =
                mvc.perform(get("/projects/").contentType(APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertEquals(
                objectMapper.writeValueAsString(projectDtos),
                resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void get_project_by_id_status_200() throws Exception {
        Project project = new Project();
        project.setName("Project");
        project.setId(1);

        when(service.findById(project.getId())).thenReturn(project);

        mvc.perform(get("/projects/" + project.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_project_by_id() throws Exception {
        Project project = new Project();
        project.setName("Project");
        project.setId(1);

        when(service.findById(project.getId())).thenReturn(project);

        String actual = mvc.perform(get("/projects/" + project.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(project), actual);
    }

    @Test
    void get_project_by_id_should_return_null() throws Exception {
        when(service.findById(Mockito.anyInt())).thenReturn(null);
        String contentAsString = mvc.perform(get("/projects/0").contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals("", contentAsString);
    }

    @Test
    void put_project_should_return_status200() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project editedProject = new Project();
        editedProject.setId(1);
        editedProject.setName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedProject);
        when(service.findById(dto.getId())).thenReturn(editedProject);
        when(service.save(editedProject)).thenReturn(editedProject);


        mvc.perform(put("/projects/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void put_project_should_return_status201() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project editedProject = new Project();
        editedProject.setId(1);
        editedProject.setName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedProject);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedProject)).thenReturn(editedProject);


        mvc.perform(put("/projects/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void put_exist_project() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project editedProject = new Project();
        editedProject.setId(1);
        editedProject.setName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedProject);
        when(service.findById(dto.getId())).thenReturn(editedProject);
        when(service.save(editedProject)).thenReturn(editedProject);

        String actual = mvc.perform(put("/projects/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedProject), actual);
    }


    @Test
    void put_new_project() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project editedProject = new Project();
        editedProject.setId(1);
        editedProject.setName("Edited");

        when(mapper.toEntity(any())).thenReturn(editedProject);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedProject)).thenReturn(editedProject);

        String actual = mvc.perform(
                put("/projects/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedProject), actual);
    }

    @Test
    void post_add_new_project_status201() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project entity = new Project();
        entity.setName("Project");
        entity.setId(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        mvc.perform(
                post("/projects/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());

    }


    @Test
    void post_add_new_project() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setName("Project");
        dto.setId(1);

        Project entity = new Project();
        entity.setName("Project");
        entity.setId(1);

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        String actual = mvc.perform(
                post("/projects/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(entity), actual);
    }

    @Test
    void delete_project_by_id_status204() throws Exception {

        Project project = new Project();
        project.setId(1);

        when(service.findById(1)).thenReturn(project);

        mvc.perform(
                delete("/projects/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(service,times(1)).delete(project);
    }

    @Test
    void delete_project_by_id_status404() throws Exception {



        when(service.findById(1)).thenReturn(null);

        mvc.perform(
                delete("/projects/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNotFound());

    }

}
