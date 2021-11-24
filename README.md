# hivemq-kafka-ebaa

HiveMQ-Kafka is a Java Project that building an IoT data pipeline, using: 
- <strong> HiveMQ Cloud as a MQTT Broker </strong> for receiving the data from multiple smart IoT devices (in our case user smart phones).
- <strong> Cloudkarafka  as a free cloud kafka connector </strong> for streaming the data collected through MQTT and processing it.

This project is part of my mater thesis in Big Data and IoT. 

## Requirements
- Have a JDK installation on your system. Either set the JAVA_HOME environment variable pointing to your JDK installation or have the java executable on your PATH.
- Have Maven installed

## Installation

```bash
mvn clean compile exec:java
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.