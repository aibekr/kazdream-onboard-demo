package kz.kazdream.demo.onboard.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import kz.kazdream.demo.onboard.db.dto.ProjectDto;
import kz.kazdream.demo.onboard.db.entity.Project;
import kz.kazdream.demo.onboard.db.mapper.ProjectMapper;
import kz.kazdream.demo.onboard.db.service.ProjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class ProjectRabbit {
    private final static String QUEUE_NAME = "create_project";

    private final ProjectService service;

    private final ProjectMapper projectMapper;

    public ProjectRabbit(ProjectService service, ProjectMapper projectMapper, @Value("${rabbitmq.host}") String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        ObjectMapper mapper = new ObjectMapper();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ProjectDto dto = mapper.readValue(delivery.getBody(), ProjectDto.class);
            createProject(dto);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

        this.service = service;
        this.projectMapper = projectMapper;
    }

    private Project createProject(ProjectDto dto){
        return service.save(projectMapper.toEntity(dto));
    }
}
