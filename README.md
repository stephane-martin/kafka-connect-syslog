This connector provides support for receiving messages via syslog. The 0.0.2 version did break compatibility with existing schemas. 
A namespace was added to the connect schema.
  
| Name                         | Description                                                                                                                   | Type    | Default | Valid Values | Importance |
|------------------------------|-------------------------------------------------------------------------------------------------------------------------------|---------|---------|--------------|------------|
| kafka.topic                  | Kafka topic to write syslog data to.                                                                                          | string  |         |              | high       |
| syslog.port                  | Port to listen on.                                                                                                            | int     |         |              | high       |
| syslog.host                  | Hostname to listen on.                                                                                                        | string  | null    |              | high       |
| backoff.ms                   | Number of milliseconds to sleep when no data is returned.                                                                     | int     | 500     | [50,...]     | low        |
| syslog.charset               | Character set for syslog messages.                                                                                            | string  | UTF-8   |              | low        |
| syslog.reverse.dns.cache.ms  | The amount of time to cache the reverse lookup values from DNS.                                                               | long    | 60000   |              | low        |
| syslog.reverse.dns.remote.ip | Flag to determine if the ip address of the remote sender should be resolved. If set to false the hostname value will be null. | boolean | false   |              | low        |
| syslog.shutdown.wait         | The amount of time in milliseconds to wait for messages when shutting down the server.                                        | long    | 500     |              | low        |
| syslog.structured.data       | Flag to determine if structured data should be used.                                                                          | boolean | false   |              | low        |

## Minimal Example

```
name=udpsyslog
tasks.max=2
connector.class=io.confluent.kafka.connect.syslog.source.UDPSyslogSourceConnector
kafka.topic=syslog-udp
syslog.port=5514
```

# TCP Syslog Source

| Name                               | Description                                                                                                                   | Type    | Default | Valid Values | Importance |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|---------|---------|--------------|------------|
| kafka.topic                        | Kafka topic to write syslog data to.                                                                                          | string  |         |              | high       |
| syslog.port                        | Port to listen on.                                                                                                            | int     |         |              | high       |
| syslog.host                        | Hostname to listen on.                                                                                                        | string  | null    |              | high       |
| backoff.ms                         | Number of milliseconds to sleep when no data is returned.                                                                     | int     | 500     | [50,...]     | low        |
| syslog.backlog                     | Number of connections to allow in backlog.                                                                                    | int     | 50      | [1,...]      | low        |
| syslog.charset                     | Character set for syslog messages.                                                                                            | string  | UTF-8   |              | low        |
| syslog.max.active.sockets          | Maximum active sockets                                                                                                        | int     | 0       |              | low        |
| syslog.max.active.sockets.behavior | Maximum active sockets                                                                                                        | int     | 0       |              | low        |
| syslog.reverse.dns.cache.ms        | The amount of time to cache the reverse lookup values from DNS.                                                               | long    | 60000   |              | low        |
| syslog.reverse.dns.remote.ip       | Flag to determine if the ip address of the remote sender should be resolved. If set to false the hostname value will be null. | boolean | false   |              | low        |
| syslog.shutdown.wait               | The amount of time in milliseconds to wait for messages when shutting down the server.                                        | long    | 500     |              | low        |
| syslog.structured.data             | Flag to determine if structured data should be used.                                                                          | boolean | false   |              | low        |
| syslog.timeout                     | Number of milliseconds before a timing out the connection.                                                                    | int     | 0       |              | low        |

## Minimal Example 

```
name=tcpsyslog
tasks.max=2
connector.class=io.confluent.kafka.connect.syslog.source.TCPSyslogSourceConnector
kafka.topic=syslog-tcp
syslog.port=5514
```

# SSL TCP Syslog Source

| Name                               | Description                                                                                                                   | Type     | Default | Valid Values | Importance |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|----------|---------|--------------|------------|
| kafka.topic                        | Kafka topic to write syslog data to.                                                                                          | string   |         |              | high       |
| syslog.keystore                    | Path to the keystore containing the ssl certificate for this host.                                                            | string   |         |              | high       |
| syslog.keystore.password           | Password for the keystore.                                                                                                    | password |         |              | high       |
| syslog.port                        | Port to listen on.                                                                                                            | int      |         |              | high       |
| syslog.truststore                  | Path to the truststore containing the ssl certificate for this host.                                                          | string   |         |              | high       |
| syslog.truststore.password         | Password for the truststore.                                                                                                  | password |         |              | high       |
| syslog.host                        | Hostname to listen on.                                                                                                        | string   | null    |              | high       |
| backoff.ms                         | Number of milliseconds to sleep when no data is returned.                                                                     | int      | 500     | [50,...]     | low        |
| syslog.backlog                     | Number of connections to allow in backlog.                                                                                    | int      | 50      | [1,...]      | low        |
| syslog.charset                     | Character set for syslog messages.                                                                                            | string   | UTF-8   |              | low        |
| syslog.max.active.sockets          | Maximum active sockets                                                                                                        | int      | 0       |              | low        |
| syslog.max.active.sockets.behavior | Maximum active sockets                                                                                                        | int      | 0       |              | low        |
| syslog.reverse.dns.cache.ms        | The amount of time to cache the reverse lookup values from DNS.                                                               | long     | 60000   |              | low        |
| syslog.reverse.dns.remote.ip       | Flag to determine if the ip address of the remote sender should be resolved. If set to false the hostname value will be null. | boolean  | false   |              | low        |
| syslog.shutdown.wait               | The amount of time in milliseconds to wait for messages when shutting down the server.                                        | long     | 500     |              | low        |
| syslog.structured.data             | Flag to determine if structured data should be used.                                                                          | boolean  | false   |              | low        |
| syslog.timeout                     | Number of milliseconds before a timing out the connection.                                                                    | int      | 0       |              | low        |

## Minimal Example

```
name=tcpsyslog
tasks.max=2
connector.class=io.confluent.kafka.connect.syslog.source.SSLTCPSyslogSourceConnector
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