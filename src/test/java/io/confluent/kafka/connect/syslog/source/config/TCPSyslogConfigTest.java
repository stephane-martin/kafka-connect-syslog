/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.confluent.kafka.connect.syslog.source.config;

import io.confluent.kafka.connect.utils.config.MarkdownFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class TCPSyslogConfigTest {

  final static String HOST = "0.0.0.0";
  final static Integer PORT = 5514;
  final static Integer BACKLOG = 1024;
  final static Integer MAXACTIVESOCKETS = 65535;
  final static Byte MAXACTIVESOCKETSBEHAVIOR = 0;
  final static String KAFKA_TOPIC = "syslog";

  TCPSyslogSourceConfig config;
  Map<String, String> params;


  public static TCPSyslogSourceConfig config() {
    Map<String, String> params;
    params = new LinkedHashMap<>();
    params.put(BaseSyslogSourceConfig.PORT_CONFIG, PORT.toString());
    params.put(BaseSyslogSourceConfig.HOST_CONFIG, HOST);
    params.put(BaseSyslogSourceConfig.TOPIC_CONFIG, KAFKA_TOPIC);
    params.put(TCPSyslogSourceConfig.BACKLOG_CONFIG, BACKLOG.toString());
    params.put(TCPSyslogSourceConfig.MAX_ACTIVE_SOCKETS_BEHAVIOR_CONFIG, MAXACTIVESOCKETSBEHAVIOR.toString());
    params.put(TCPSyslogSourceConfig.MAX_ACTIVE_SOCKETS_CONFIG, MAXACTIVESOCKETS.toString());
    params.put(TCPSyslogSourceConfig.REVERSE_DNS_IP_CONF, "true");
    return new TCPSyslogSourceConfig(params);
  }

  @Before
  public void setup() {
    this.params = new LinkedHashMap<>();
    this.params.put(BaseSyslogSourceConfig.PORT_CONFIG, PORT.toString());
    this.params.put(BaseSyslogSourceConfig.HOST_CONFIG, HOST);
    this.params.put(BaseSyslogSourceConfig.TOPIC_CONFIG, KAFKA_TOPIC);
    this.params.put(TCPSyslogSourceConfig.BACKLOG_CONFIG, BACKLOG.toString());
    this.params.put(TCPSyslogSourceConfig.MAX_ACTIVE_SOCKETS_BEHAVIOR_CONFIG, MAXACTIVESOCKETSBEHAVIOR.toString());
    this.params.put(TCPSyslogSourceConfig.MAX_ACTIVE_SOCKETS_CONFIG, MAXACTIVESOCKETS.toString());
    this.config = new TCPSyslogSourceConfig(this.params);
  }

  @Test
  public void doc() {
    System.out.println(MarkdownFormatter.toMarkdown(TCPSyslogSourceConfig.getConfig()));
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
