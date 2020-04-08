package kz.kazdream.demo.onboard.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Controller
public class ProducerHelper {

    private final String host;

    public ProducerHelper( @Value("${rabbitmq.host}") String host) {
        this.host = host;
    }

    public void send(String queueName, byte[] object) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, object);

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
