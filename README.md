Initial implementation of a syslog source for Kafka Connect.

Unfortunately this requires the following [pull request](https://github.com/Graylog2/syslog4j-graylog2/pull/9) from [syslog4j-graylog2](https://github.com/Graylog2/syslog4j-graylog2). You will need to pull do this pull request and `mvn clean install` before building this project.  

# UDP Syslog Source

## Minimal Example

```
name=udpsyslog
tasks.max=2
connector.class=io.confluent.connect.syslog.source.UDPSyslogSourceConnector
kafka.topic=syslog-udp
syslog.port=5514
```

# TCP Syslog Source

## Minimal Example 

```
name=tcpsyslog
tasks.max=2
connector.class=io.confluent.connect.syslog.source.TCPSyslogSourceConnector
kafka.topic=syslog-tcp
syslog.port=5514
```

# SSL TCP Syslog Source

## Minimal Example

```
name=tcpsyslog
tasks.max=2
connector.class=io.confluent.connect.syslog.source.SSLTCPSyslogSourceConnector
kafka.topic=syslog-tcp
syslog.port=5514
syslog.keystore=/etc/security/keystore.tks
syslog.keystore.password=90e4ngghadfghi
syslog.truststore=/etc/security/truststore.tks
syslog.truststore.password=oznsdfgnsdfg
```

# Development 

``` 
mvn clean package
export CLASSPATH="$(find `pwd`/target/kafka-connect-syslog-1.0.0-SNAPSHOT-package/share/java/kafka-connect-syslog -type f -name '*.jar' | tr '\n' ':')"
export KAFKA_JMX_OPTS='-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005'
mvn clean package && $CONFLUENT_HOME/bin/connect-standalone $CONFLUENT_HOME/etc/schema-registry/connect-avro-standalone.properties config/tcpsyslog.properties
```