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

import io.confluent.kafka.connect.syslog.source.config.BaseSyslogSourceConfig;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.source.SourceRecord;
import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.SyslogServerSessionlessEventHandlerIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;


class ConnectSyslogEventHandler implements SyslogServerSessionlessEventHandlerIF {
  public static final String HOST = "host";
  public static final String FACILITY = "facility";
  public static final String DATE = "date";
  public static final String LEVEL = "level";
  public static final String MESSAGE = "message";
  public static final String CHARSET = "charset";
  public static final String REMOTE_ADDRESS = "remote_address";
  public static final String HOSTNAME = "hostname";
  private static Logger log = LoggerFactory.getLogger(ConnectSyslogEventHandler.class);
  final Schema keySchema;
  final Schema valueSchema;
  final ConcurrentLinkedDeque<SourceRecord> recordQueue;
  final BaseSyslogSourceConfig config;
  HostnameResolver hostnameResolver;

  public ConnectSyslogEventHandler(ConcurrentLinkedDeque<SourceRecord> recordQueue, BaseSyslogSourceConfig config) {
    this.recordQueue = recordQueue;
    this.config = config;

    this.keySchema = SchemaBuilder.struct().name("io.confluent.kafka.connect.syslog.source.SyslogKey")
        .field(REMOTE_ADDRESS, Schema.STRING_SCHEMA)
        .build();

    this.valueSchema = SchemaBuilder.struct().name("io.confluent.kafka.connect.syslog.source.SyslogValue")
        .field(DATE, Timestamp.builder().optional().build())
        .field(FACILITY, Schema.OPTIONAL_INT32_SCHEMA)
        .field(HOST, Schema.OPTIONAL_STRING_SCHEMA)
        .field(LEVEL, Schema.OPTIONAL_INT32_SCHEMA)
        .field(MESSAGE, Schema.OPTIONAL_STRING_SCHEMA)
        .field(CHARSET, Schema.OPTIONAL_STRING_SCHEMA)
        .field(REMOTE_ADDRESS, Schema.OPTIONAL_STRING_SCHEMA)
        .field(HOSTNAME, Schema.OPTIONAL_STRING_SCHEMA)
        .build();

    this.hostnameResolver = new HostnameResolverImpl(this.config);
  }

  @Override
  public void event(SyslogServerIF syslogServerIF, SocketAddress socketAddress, SyslogServerEventIF event) {
    if (log.isDebugEnabled()) {
      log.debug("event received from {}", socketAddress);
    }

    Map<String, String> partition = Collections.singletonMap(HOST, event.getHost());
    Map<String, String> sourceOffset = new HashMap<>();
    String remoteAddress = socketAddress.toString();

    Struct keyStruct = new Struct(this.keySchema)
        .put(REMOTE_ADDRESS, remoteAddress);

    Struct valueStruct = new Struct(this.valueSchema)
        .put(DATE, event.getDate())
        .put(FACILITY, event.getFacility())
        .put(HOST, event.getHost())
        .put(LEVEL, event.getLevel())
        .put(MESSAGE, event.getMessage())
        .put(CHARSET, event.getCharSet())
        .put(REMOTE_ADDRESS, remoteAddress)
        .put(FACILITY, event.getFacility());


    if (this.config.reverseDnsIP()) {
      try {
        InetSocketAddress resolvedAddress = this.hostnameResolver.resolve(socketAddress);
        valueStruct.put(HOSTNAME, resolvedAddress.getHostName());
      } catch (Exception ex) {
        if (log.isWarnEnabled()) {
          log.warn("Exception while doing a reverse lookup of {}", socketAddress, ex);
        }
      }
    } else {
      if (socketAddress instanceof InetSocketAddress) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        valueStruct.put(HOSTNAME, inetSocketAddress.getAddress().getHostAddress());
      } else {
        valueStruct.put(HOSTNAME, socketAddress.toString());
      }
    }

    SourceRecord sourceRecord = new SourceRecord(
        partition,
        sourceOffset,
        this.config.topic(),
        null,
        this.keySchema,
        keyStruct,
        this.valueSchema,
        valueStruct
    );
    this.recordQueue.add(sourceRecord);
  }

  @Override
  public void exception(SyslogServerIF syslogServerIF, SocketAddress socketAddress, Exception e) {
    if (log.isErrorEnabled()) {
      log.error("Exception throw " + socketAddress, e);
    }
  }

  @Override
  public void initialize(SyslogServerIF syslogServerIF) {

  }

  @Override
  public void destroy(SyslogServerIF syslogServerIF) {

  }
}
