package io.confluent.connect.syslog.source.config;

import org.graylog2.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;

public class TCPSyslogConfigTest {

  final String HOST = "0.0.0.0";
  final Integer PORT = 5514;
  final Integer BACKLOG = 1024;
  final Integer MAXACTIVESOCKETS = 65535;
  final Byte MAXACTIVESOCKETSBEHAVIOR = 0;
  final String KAFKA_TOPIC="syslog";

  TCPSyslogConfig config;
  Map<String, String> params;

  @Before
  public void setup(){
    this.params = new LinkedHashMap<>();
    this.params.put(BaseSyslogConfig.PORT_CONFIG, PORT.toString());
    this.params.put(BaseSyslogConfig.HOST_CONFIG, HOST);
    this.params.put(BaseSyslogConfig.TOPIC_CONFIG, KAFKA_TOPIC);
    this.params.put(TCPSyslogConfig.BACKLOG_CONFIG, BACKLOG.toString());
    this.params.put(TCPSyslogConfig.MAX_ACTIVE_SOCKETS_BEHAVIOR_CONFIG, MAXACTIVESOCKETSBEHAVIOR.toString());
    this.params.put(TCPSyslogConfig.MAX_ACTIVE_SOCKETS_CONFIG, MAXACTIVESOCKETS.toString());
    this.config = new TCPSyslogConfig(this.params);
  }



//  @Test
//  public void getSyslogServerConfig(){
//
//    Assert.assertNotNull("actual should not be null.", actual);
//    Assert.assertThat(actual, instanceOf(TCPNetSyslogServerConfig.class));
//    TCPNetSyslogServerConfig actualConfig = (TCPNetSyslogServerConfig) actual;
//    Assert.assertEquals("Port does not match.", (int)PORT, actualConfig.getPort());
//    Assert.assertEquals("HOST does not match.", HOST, actualConfig.getHost());
//
//    Assert.assertEquals("Backlog does not match.", (int)BACKLOG, actualConfig.getBacklog());
//    Assert.assertEquals("MaxActiveSockets does not match.", (int)MAXACTIVESOCKETS, actualConfig.getMaxActiveSockets());
//    Assert.assertEquals("MaxActiveSocketsBehavior does not match.", (Byte)MAXACTIVESOCKETSBEHAVIOR, (Byte)actualConfig.getMaxActiveSocketsBehavior());
//  }



}
