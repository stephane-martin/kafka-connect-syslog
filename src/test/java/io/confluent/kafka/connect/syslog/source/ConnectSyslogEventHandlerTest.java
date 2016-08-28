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
package io.confluent.kafka.connect.syslog.source;

import io.confluent.kafka.connect.syslog.source.config.TCPSyslogConfigTest;
import io.confluent.kafka.connect.syslog.source.config.TCPSyslogSourceConfig;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectSyslogEventHandlerTest {
  TCPSyslogSourceConfig config;

  @Before
  public void setup() {
    this.config = TCPSyslogConfigTest.config();
  }


  @Test
  public void event() {
    final ConcurrentLinkedDeque<SourceRecord> messageQueue = new ConcurrentLinkedDeque<>();
    ConnectSyslogEventHandler handler = new ConnectSyslogEventHandler(messageQueue, this.config);

    SyslogServerIF syslogServerIF = mock(SyslogServerIF.class);
    SyslogServerEventIF event = mock(SyslogServerEventIF.class);
    SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 5432);


    when(event.getDate()).thenReturn(new Date());
    when(event.getFacility()).thenReturn(1);
    when(event.getHost()).thenReturn("hostname.example.com");
    when(event.getLevel()).thenReturn(2);
    when(event.getMessage()).thenReturn("This is a test message");
    when(event.getCharSet()).thenReturn("UTF8");

    handler.event(syslogServerIF, socketAddress, event);
    assertFalse("messageQueue should not be empty.", messageQueue.isEmpty());
    SourceRecord sourceRecord = messageQueue.poll();
    assertTrue("value should be struct", sourceRecord.value() instanceof Struct);


    final Struct valueStruct = (Struct) sourceRecord.value();

    assertEquals("Facility does not match.", event.getFacility(), valueStruct.get(ConnectSyslogEventHandler.FACILITY));
    assertEquals("Date does not match.", event.getDate(), valueStruct.get(ConnectSyslogEventHandler.DATE));
    assertEquals("Level does not match.", event.getLevel(), valueStruct.get(ConnectSyslogEventHandler.LEVEL));
    assertEquals("Host does not match.", event.getHost(), valueStruct.get(ConnectSyslogEventHandler.HOST));
//    assertEquals("HostStrippedFromMessage does not match.", place.isHostStrippedFromMessage(), struct.get("HostStrippedFromMessage"));
    assertEquals("Message does not match.", event.getMessage(), valueStruct.get(ConnectSyslogEventHandler.MESSAGE));
    assertEquals("CharSet does not match.", event.getCharSet(), valueStruct.get(ConnectSyslogEventHandler.CHARSET));
    assertEquals("Hostname does not match.", "google-public-dns-a.google.com", valueStruct.get(ConnectSyslogEventHandler.HOSTNAME));

  }


}
