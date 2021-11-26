package example;

import helpers.JsonHelper;
import lombok.val;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pulsar.Event;
import okhttp3.*;
import pulsar.TeamsMsg;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {
    static final String PULSAR_URL = System.getenv("PULSAR_URL") != null && !Objects.equals(System.getenv("PULSAR_URL"), "") ? System.getenv("PULSAR_URL") : "pulsar://localhost:6650";
    static PulsarClient pulsarClient;
    static OkHttpClient httpClient;
    static final String topicName = System.getenv("PULSAR_TOPIC_NAME") != null && !Objects.equals(System.getenv("PULSAR_TOPIC_NAME"), "") ? System.getenv("PULSAR_TOPIC_NAME") : "Hackathon.Producer.Green.Employees";
    static final String subscriptionName = System.getenv("PULSAR_SUBSCRIPTION_NAME") != null && !Objects.equals(System.getenv("PULSAR_SUBSCRIPTION_NAME"), "") ? System.getenv("PULSAR_SUBSCRIPTION_NAME") : "Hackathon.Team13.Consumer";
    static final String webhookUrl = System.getenv("WEBHOOK_URL") != null && !Objects.equals(System.getenv("WEBHOOK_URL"), "") ? System.getenv("WEBHOOK_URL") : "https://legogroup.webhook.office.com/webhookb2/5eb74615-cb59-411f-bad3-171e73a26780@1d063515-6cad-4195-9486-ea65df456faa/IncomingWebhook/d1725978b3bf4324b3b1cae2eb34c221/fedf98be-f3ca-43a2-a0cb-add3cffda1d7";

    static {
        try {
            pulsarClient = PulsarClient.builder().serviceUrl(PULSAR_URL).build();

        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

        httpClient = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .build();
    }

    @BeforeAll
    static void setup() throws PulsarClientException {
        assert topicName != null;

        Producer<Event> producer = pulsarClient.newProducer(Schema.JSON(Event.class)).topic(topicName).create();
        producer.send(new Event(391, "Jeff Tian", "jeff-tian", "CDT", "added", new Date(), false));
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

                val request = new Request.Builder()
                        .url(webhookUrl)
                        .header("Content-Type", "application/json")
                        .post(RequestBody.create(JsonHelper.stringify(new TeamsMsg(msg.getValue().toString())), MediaType.parse("application/json")))
                        .build();

                Call call = httpClient.newCall(request);
                Response response = call.execute();
                assertEquals(200, response.code());

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
