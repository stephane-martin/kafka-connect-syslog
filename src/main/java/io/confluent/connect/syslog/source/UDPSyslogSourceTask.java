package io.confluent.connect.syslog.source;

import io.confluent.connect.syslog.source.config.TCPSyslogConfig;
import io.confluent.connect.syslog.source.config.UDPSyslogConfig;

import java.util.Map;


public class UDPSyslogSourceTask extends SyslogSourceTask<UDPSyslogConfig> {
  @Override
  UDPSyslogConfig createConfig(Map<String, String> props) {
    return new UDPSyslogConfig(props);
  }
}
