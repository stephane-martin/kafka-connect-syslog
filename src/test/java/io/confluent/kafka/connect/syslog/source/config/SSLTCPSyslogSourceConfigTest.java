package io.confluent.kafka.connect.syslog.source.config;

import io.confluent.kafka.connect.utils.config.MarkdownFormatter;
import org.junit.Test;

public class SSLTCPSyslogSourceConfigTest {
  @Test
  public void doc() {
    System.out.println(MarkdownFormatter.toMarkdown(SSLTCPSyslogSourceConfig.getConfig()));
  }
}
