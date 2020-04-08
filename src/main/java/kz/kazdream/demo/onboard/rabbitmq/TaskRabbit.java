package kz.kazdream.demo.onboard.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import kz.kazdream.demo.onboard.db.dto.TaskDto;
import kz.kazdream.demo.onboard.db.entity.Task;
import kz.kazdream.demo.onboard.db.mapper.TaskMapper;
import kz.kazdream.demo.onboard.db.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class TaskRabbit {
    private final static String QUEUE_NAME = "create_task";

    private final TaskService service;

    private final TaskMapper projectMapper;

    public TaskRabbit(TaskService service, TaskMapper projectMapper, @Value("${rabbitmq.host}") String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        ObjectMapper mapper = new ObjectMapper();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            TaskDto dto = mapper.readValue(delivery.getBody(), TaskDto.class);
            createTask(dto);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });

        this.service = service;
        this.projectMapper = projectMapper;
    }

    private Task createTask(TaskDto dto) {
        return service.save(projectMapper.toEntity(dto));
    }
}
