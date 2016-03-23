package io.confluent.connect.syslog.source;

import io.confluent.connect.syslog.source.config.SSLTCPSyslogConfig;
import io.confluent.connect.syslog.source.config.TCPSyslogConfig;

import java.util.Map;


public class SSLTCPSyslogSourceTask extends SyslogSourceTask<SSLTCPSyslogConfig> {
  @Override
  SSLTCPSyslogConfig createConfig(Map<String, String> props) {
    return new SSLTCPSyslogConfig(props);
  }
}
