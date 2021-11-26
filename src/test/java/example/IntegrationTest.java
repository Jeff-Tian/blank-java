package example;

import lombok.val;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pulsar.Event;

import java.util.Date;
import java.util.Objects;

public class IntegrationTest {
    static final String PULSAR_URL = "pulsar://localhost:6650";
    static PulsarClient pulsarClient;
    static final String topicName = System.getenv("PULSAR_TOPIC_NAME") != null && !Objects.equals(System.getenv("PULSAR_TOPIC_NAME"), "") ? System.getenv("PULSAR_TOPIC_NAME") : "Hackathon.Producer.Green.Employees";
    static final String subscriptionName = System.getenv("PULSAR_SUBSCRIPTION_NAME") != null && !Objects.equals(System.getenv("PULSAR_SUBSCRIPTION_NAME"), "") ? System.getenv("PULSAR_SUBSCRIPTION_NAME") : "Hackathon.Team13.Consumer";

    static {
        try {
            pulsarClient = PulsarClient.builder().serviceUrl(PULSAR_URL).build();

        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void setup() throws PulsarClientException {
        assert topicName != null;

        Producer<Event> producer = pulsarClient.newProducer(Schema.JSON(Event.class)).topic(topicName).create();
        producer.send(new Event(391, "Glen Carroll", "tinny", "TeamX", "added", new Date(), false));
        producer.close();
    }

    @Test
    void consumeEvents() throws PulsarClientException {
        val consumer = pulsarClient.newConsumer(Schema.JSON(Event.class))
                .topic(topicName).subscriptionName(subscriptionName).subscribe();

        while (true) {
            val msg = consumer.receive();
            try {
                System.out.println("Received: " + msg.getValue());



                consumer.acknowledge(msg);
                break;
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
            } finally {
                consumer.close();
            }
        }
    }
}
