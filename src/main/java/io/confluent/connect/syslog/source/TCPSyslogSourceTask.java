package io.confluent.connect.syslog.source;

import io.confluent.connect.syslog.source.config.TCPSyslogConfig;

import java.util.Map;


public class TCPSyslogSourceTask extends SyslogSourceTask<TCPSyslogConfig> {
  @Override
  TCPSyslogConfig createConfig(Map<String, String> props) {
    return new TCPSyslogConfig(props);
  }
}
