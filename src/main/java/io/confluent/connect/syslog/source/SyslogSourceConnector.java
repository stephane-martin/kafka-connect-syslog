package io.confluent.connect.syslog.source;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class SyslogSourceConnector extends SourceConnector {

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  Map<String, String> props;

  @Override
  public void start(Map<String, String> props) {
    this.props = props;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    List<Map<String, String>> tasks = new ArrayList<>();
    tasks.add(this.props);
    return tasks;
  }

  @Override
  public void stop() {

  }
}
