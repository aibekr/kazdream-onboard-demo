package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.ProjectDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.mapper.ProjectMapper;
import kz.kazdream.demo.onboard.db.service.ProjectService;
import kz.kazdream.demo.onboard.rabbitmq.ProducerHelper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/projects")
public class ProjectApi {

    private final ProjectService projectService;

    private final ProjectMapper mapper;

    private final ProducerHelper helper;

    public ProjectApi(ProjectService projectService, ProjectMapper mapper, ProducerHelper helper) {
        this.projectService = projectService;
        this.mapper = mapper;
        this.helper = helper;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getProjects() {
        //TODO pagination

        List<Project> projects = projectService.findAll();

        return ResponseEntity.ok(mapper.toDtoList(projects));
    }

    @GetMapping("{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable int projectId) {
        return ResponseEntity.ok(projectService.findById(projectId));
    }

    @PutMapping(value = "{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> editProject(@PathVariable int projectId, @RequestBody ProjectDto projectDto) {
        Project project = mapper.toEntity(projectDto);

        if (projectService.findById(projectId) == null) {
            return ResponseEntity.status(CREATED).body(projectService.save(project));
        }

        return ResponseEntity.ok(projectService.save(project));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto) {
        Project project = mapper.toEntity(projectDto);
        return ResponseEntity.status(CREATED).body(projectService.save(project));
    }


    @PostMapping(value = "/rabbit",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> createProjectRabbit(@RequestBody ProjectDto projectDto) throws JsonProcessingException {
        Project project = mapper.toEntity(projectDto);
        ObjectMapper objectMapper = new ObjectMapper();
        helper.send("create_project",objectMapper.writeValueAsBytes(project));
        return ResponseEntity.status(CREATED).body(null);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Project> deleteProject(@PathVariable int projectId){
        Project project = projectService.findById(projectId);
        if (project == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        projectService.delete(project);

        return ResponseEntity.status(NO_CONTENT).body(null);
    }
}