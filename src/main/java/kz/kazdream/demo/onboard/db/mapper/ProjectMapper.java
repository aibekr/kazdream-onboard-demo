package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.ProjectDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.service.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper extends AbstractMapper<Project, ProjectDto> {
    private final TaskService taskService;

    public ProjectMapper(TaskService taskService) {
        super(Project.class, ProjectDto.class);
        this.taskService = taskService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper
                .createTypeMap(Project.class, ProjectDto.class)
                .addMappings(m -> m.skip(ProjectDto::setTaskIds))
                .setPostConverter(toDtoConverter());
        mapper
                .createTypeMap(ProjectDto.class, Project.class)
                .addMappings(m -> m.skip(Project::setTasks))
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Project source, ProjectDto destination) {
        if (source.getTasks() != null) {
            destination.setTaskIds(
                    source.getTasks().stream().map(Task::getId).mapToInt(i -> i).toArray());
        }
    }

    @Override
    public void mapSpecificFields(ProjectDto source, Project destination) {
        if (source.getTaskIds() != null) {
            List<Integer> taskIds =
                    Arrays.stream(source.getTaskIds()).boxed().collect(Collectors.toList());
            List<Task> tasks = taskService.findAllById(taskIds);
            destination.setTasks(tasks);
        }
    }

    @Override
    public List<ProjectDto> toDtoList(List<Project> entityList) {
        List<ProjectDto> dtos = new ArrayList<>();
        for (Project entity : entityList) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    @Override
    public List<Project> toEntityList(List<ProjectDto> entityDto) {
        List<Project> listEntity = new ArrayList<>();
        for (ProjectDto dto : entityDto) {
            listEntity.add(toEntity(dto));
        }
        return listEntity;
    }
}
