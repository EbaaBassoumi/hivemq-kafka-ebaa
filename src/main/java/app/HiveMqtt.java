package app;

import com.google.gson.Gson;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HiveMqtt {

    final String host = "7434fa56e15249ef8c9ba5a92777893b.s1.eu.hivemq.cloud";
    final String username = "ebaabassoumi";
    final String password = "Ebaa1993";
    final Mqtt5BlockingClient client;

    public HiveMqtt() {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");    // assuming that the Kafka broker this application is talking to runs on local machine with port 9092
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        //create an MQTT client
        client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();
    }

    public Mqtt5BlockingClient connect(){
        //connect to HiveMQ Cloud with TLS and username/pw
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

        //subscribe to the topic "my/test/topic"
        client.subscribeWith()
                .topicFilter("owntracks/#")
                .send();


        return client;

    }


    public void publishToCloudDemoData() throws FileNotFoundException {

        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/source_demo_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
                //publish a message to the topic "my/test/topic"
                client.publishWith()
                        .topic("owntracks/demo/data")
                        .payload(UTF_8.encode(line))
                        .send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
