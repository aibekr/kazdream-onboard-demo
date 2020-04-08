package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.TaskDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper extends AbstractMapper<Task, TaskDto> {
    private final ProjectService projectService;

    public TaskMapper(ProjectService projectService) {
        super(Task.class, TaskDto.class);
        this.projectService = projectService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper
                .createTypeMap(Task.class, TaskDto.class)
                .addMappings(m -> m.skip(TaskDto::setProjectId))
                .setPostConverter(toDtoConverter());
        mapper
                .createTypeMap(TaskDto.class, Task.class)
                .addMappings(m -> m.skip(Task::setProject))
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Task source, TaskDto destination) {
        if (source.getProject() != null) {
            destination.setProjectId(source.getProject().getId());
        }
    }

    @Override
    public void mapSpecificFields(TaskDto source, Task destination) {
        Project project = projectService.findById(source.getProjectId());
        destination.setProject(project);
    }

    @Override
    public List<TaskDto> toDtoList(List<Task> entityList) {
        List<TaskDto> dtos = new ArrayList<TaskDto>();
        for (Task entity : entityList) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    @Override
    public List<Task> toEntityList(List<TaskDto> entityDto) {
        List<Task> listEntity = new ArrayList<Task>();
        for (TaskDto dto : entityDto) {
            listEntity.add(toEntity(dto));
        }
        return listEntity;
    }
}
