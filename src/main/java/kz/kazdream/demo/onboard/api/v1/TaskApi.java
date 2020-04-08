package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.TaskDto;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.mapper.TaskMapper;
import kz.kazdream.demo.onboard.db.service.TaskService;
import kz.kazdream.demo.onboard.rabbitmq.ProducerHelper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/tasks")
public class TaskApi {

    private final TaskService taskService;

    private final TaskMapper mapper;

    private final ProducerHelper helper;

    public TaskApi(TaskService taskService, TaskMapper mapper, ProducerHelper helper) {
        this.taskService = taskService;
        this.mapper = mapper;
        this.helper = helper;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks() {
        List<Task> tasks = taskService.findAll();

        return ResponseEntity.ok(mapper.toDtoList(tasks));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable int taskId) {
        return ResponseEntity.ok(taskService.findById(taskId));
    }

    @PutMapping(value = "{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> editTask(@PathVariable int taskId, @RequestBody TaskDto taskDto) {
        Task task = mapper.toEntity(taskDto);

        if (taskService.findById(taskId) == null) {
            return ResponseEntity.status(CREATED).body(taskService.save(task));
        }

        return ResponseEntity.ok(taskService.save(task));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
        Task task = mapper.toEntity(taskDto);
        return ResponseEntity.status(CREATED).body(taskService.save(task));
    }

    @PostMapping(value = "/rabbit",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTaskRabbit(@RequestBody TaskDto taskDto) throws JsonProcessingException {
        Task project = mapper.toEntity(taskDto);
        ObjectMapper objectMapper = new ObjectMapper();
        helper.send("create_task",objectMapper.writeValueAsBytes(project));
        return ResponseEntity.status(CREATED).body(null);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable int taskId){
        Task task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        taskService.delete(task);

        return ResponseEntity.status(NO_CONTENT).body(null);
    }
}
