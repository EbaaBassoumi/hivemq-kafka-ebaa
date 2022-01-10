# hivemq-kafka-ebaa

HiveMQ-Kafka is a Java Project that building an IoT data pipeline, using: 
- <strong> HiveMQ Cloud as a MQTT Broker </strong> for receiving the data from multiple smart IoT devices (in our case user smart phones).
- <strong> Cloudkarafka  as a free cloud kafka connector </strong> for streaming the data collected through MQTT and processing it.

This project is part of my mater thesis in Big Data and IoT. 

## Requirements
- Have a JDK installation on your system. Either set the JAVA_HOME environment variable pointing to your JDK installation or have the java executable on your PATH.
- Have Maven installed

## Run ELK Locally
•	Elasticsearch
- Download the latest version of Elasticsearch from the ElasticSearch Download Page: https://www.elastic.co/downloads/elasticsearch
- Run bin\elasticsearch.bat from the command prompt.
- By default, it would start at http://localhost:9200

•	Kibana
- Download the latest distribution from the Kibana download page: https://www.elastic.co/downloads/kibana and unzip into any folder.
- Open config/kibana.yml in an editor and set elasticsearch.url to point at your Elasticsearch instance. In our case, as we will use the local instance just uncomment elasticsearch.url: http://localhost:9200
- Run bin\kibana.bat from the command prompt.
- Once started successfully, Kibana will start on default port 5601 and Kibana UI will be available at http://localhost:5601

•	Logstash
- Download the latest distribution from the Logstash download page: https://www.elastic.co/downloads/logstash
  and unzip it into any folder.
- Create one file logstash.conf as per configuration instructions. We will again come to this point during actual demo time for exact configuration.
- Now run bin/logstash -f logstash.conf to start logstash

## Installation

```bash
mvn clean compile exec:java
```

Open Kibana UI to see messages: http://localhost:5601


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
