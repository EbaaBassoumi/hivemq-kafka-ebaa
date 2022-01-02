package app;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.concurrent.atomic.AtomicInteger;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(String[] args) throws Exception {

        // Define HiveMqtt Client
        HiveMqtt hiveMqtt = new HiveMqtt();
        // Define KafkaConnector Client
        KafkaConnector kafkaConnector = new KafkaConnector();
        AtomicInteger i = new AtomicInteger();
        // Initialise the connection with the hiveMqtt client
        Mqtt5BlockingClient client = hiveMqtt.connect();
        // HiveMQTT cleint is reading data from a demo data file and publish it to the cloud
        hiveMqtt.publishToCloudDemoData();

        // set a callback that is called when a message is received (using the async API style)
        // So Basically, KAfka is listneing to HiveMQTT cloud, once a message is received, it is produced/published to Kafka Cluster
        client.toAsync().publishes(ALL, publish -> {
            System.out.println("Received message: " + publish.getTopic() + " -> " + UTF_8.decode(publish.getPayload().get()));
            kafkaConnector.produce(UTF_8.decode(publish.getPayload().get()).toString(), i.getAndIncrement());
        // disconnect the client after a message was received
        // client.disconnect();
        });
        kafkaConnector.consume();
    }
}