package app;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MyMqttMain {


    public static void main(String[] args) throws Exception {

        HiveMqtt hiveMqtt = new HiveMqtt();
        KafkaConnector kafkaConnector = new KafkaConnector();
        AtomicInteger i = new AtomicInteger();
        Mqtt5BlockingClient client = hiveMqtt.connect();
        hiveMqtt.publishToCloudDemoData();

//      set a callback that is called when a message is received (using the async API style)
        client.toAsync().publishes(ALL, publish -> {
            kafkaConnector.produce(UTF_8.decode(publish.getPayload().get()).toString(), i.getAndIncrement());
//            disconnect the client after a message was received
//            client.disconnect();
        });


        kafkaConnector.consume();
    }
}