package io.confluent.connect.syslog.source.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServer;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;
import org.graylog2.syslog4j.server.impl.net.udp.UDPNetSyslogServer;

import java.util.Map;


public class UDPSyslogConfig extends BaseSyslogConfig {
  public UDPSyslogConfig(Map<String, String> originals) {
    super(baseConfig(), originals);
  }

  @Override
  public Class getSyslogServerClass() {
    return UDPNetSyslogServer.class;
  }
}
