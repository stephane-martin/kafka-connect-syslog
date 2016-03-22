package io.confluent.connect.syslog.source;

import org.apache.kafka.connect.connector.Task;

public class TCPSyslogSourceConnector extends SyslogSourceConnector {
  @Override
  public Class<? extends Task> taskClass() {
    return TCPSyslogSourceTask.class;
  }
}
