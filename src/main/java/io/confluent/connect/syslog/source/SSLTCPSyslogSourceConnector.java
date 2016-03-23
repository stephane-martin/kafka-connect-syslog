package io.confluent.connect.syslog.source;

import org.apache.kafka.connect.connector.Task;

public class SSLTCPSyslogSourceConnector extends SyslogSourceConnector {
  @Override
  public Class<? extends Task> taskClass() {
    return SSLTCPSyslogSourceTask.class;
  }
}
