package kz.kazdream.demo.onboard.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.mapper.UserMapper;
import kz.kazdream.demo.onboard.db.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class UserRabbit {
    private final static String QUEUE_NAME = "create_user";

    private final UserService service;

    private final UserMapper projectMapper;

    public UserRabbit(UserService service, UserMapper projectMapper, @Value("${rabbitmq.host}") String host) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        ObjectMapper mapper = new ObjectMapper();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            UserDto dto = mapper.readValue(delivery.getBody(), UserDto.class);
            createUser(dto);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

        this.service = service;
        this.projectMapper = projectMapper;
    }

    private User createUser(UserDto dto){
        return service.save(projectMapper.toEntity(dto));
    }
}
