
```
export CLASSPATH="$(find `pwd`/target/kafka-connect-syslog-1.0.0-SNAPSHOT-package/share/java/kafka-connect-syslog -type f | tr '\n' ':')"

export KAFKA_JMX_OPTS='-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005'
```