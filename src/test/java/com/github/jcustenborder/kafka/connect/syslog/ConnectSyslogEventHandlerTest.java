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
package com.github.jcustenborder.kafka.connect.syslog;

import com.github.jcustenborder.kafka.connect.syslog.config.TCPSyslogSourceConfig;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectSyslogEventHandlerTest {
  TCPSyslogSourceConfig config;

  @BeforeEach
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
    assertFalse(messageQueue.isEmpty(), "messageQueue should not be empty.");
    SourceRecord sourceRecord = messageQueue.poll();
    assertTrue(sourceRecord.value() instanceof Struct, "value should be struct");

    final Struct valueStruct = (Struct) sourceRecord.value();

    assertEquals(event.getFacility(), valueStruct.get(ConnectSyslogEventHandler.FACILITY), "Facility does not match.");
    assertEquals(event.getDate(), valueStruct.get(ConnectSyslogEventHandler.DATE), "Date does not match.");
    assertEquals(event.getLevel(), valueStruct.get(ConnectSyslogEventHandler.LEVEL), "Level does not match.");
    assertEquals(event.getHost(), valueStruct.get(ConnectSyslogEventHandler.HOST), "Host does not match.");
//    assertEquals("HostStrippedFromMessage does not match.", place.isHostStrippedFromMessage(), struct.get("HostStrippedFromMessage"));
    assertEquals(event.getMessage(), valueStruct.get(ConnectSyslogEventHandler.MESSAGE), "Message does not match.");
    assertEquals(event.getCharSet(), valueStruct.get(ConnectSyslogEventHandler.CHARSET), "CharSet does not match.");
    assertEquals("google-public-dns-a.google.com", valueStruct.get(ConnectSyslogEventHandler.HOSTNAME), "Hostname does not match.");
  }
}
