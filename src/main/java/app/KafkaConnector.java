package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KafkaConnector {

    private String topic;
    private Properties props;

    final String brokers = "sulky-01.srvs.cloudkafka.com:9094,sulky-02.srvs.cloudkafka.com:9094,sulky-03.srvs.cloudkafka.com:9094";
    final String username = "b9a45yp0";
    final String password = "SSUMlcsd_D2BJ_6o11a0ohKZ6TCFFVRA";

    // SLF4J
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaConnector.class);

    public KafkaConnector() {

        this.topic = username + "-default";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
        props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", username + "-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasCfg);
    }


    public void consume() {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        List<GPSData> gpsDataList = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                String recordValue = record.value().substring(0,record.value().indexOf("}")+1);
                gpsDataList.add(gson.fromJson(recordValue, GPSData.class));
                gpsDataList.forEach(gpsData -> {
                    Map<String, Double> distanceForTheNewPoint = gpsData.geoDistance(gpsDataList);
                    if(distanceForTheNewPoint.size() > 0 ){
                        distanceForTheNewPoint.forEach((key,value) -> {
                            logger.info("Distance Less than 3 meters was detected ! {} {} ", key, value );
                        });
                    }
                });
//                logger.info("{} {} offset={}, key={}, value={}",
//                        record.topic(), record.partition(),
//                        record.offset(), record.key(), record.value());
            }
        }


    }

    public void produce(String data, int index) {
        Thread one = new Thread() {
            public void run() {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    Date date = new Date();
                    producer.send(new ProducerRecord<>(topic, Integer.toString(index), data + date.toString()));
            }
        };
        one.start();
    }

}
