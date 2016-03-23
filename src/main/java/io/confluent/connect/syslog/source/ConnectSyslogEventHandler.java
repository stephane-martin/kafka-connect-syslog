/**
 * Copyright (C) 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.confluent.connect.syslog.source;

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

import java.net.SocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


class ConnectSyslogEventHandler implements SyslogServerSessionlessEventHandlerIF {
  private static Logger log = LoggerFactory.getLogger(ConnectSyslogEventHandler.class);
  public static final String HOST="host";
  public static final String FACILITY="facility";
  public static final String DATE="date";
  public static final String LEVEL="level";
  public static final String MESSAGE="message";
  public static final String CHARSET="charset";
  public static final String REMOTE_ADDRESS="remote_address";

  final Schema keySchema;
  final Schema valueSchema;
  final BlockingQueue<SourceRecord> recordQueue;
  final String topic;

  public ConnectSyslogEventHandler(BlockingQueue<SourceRecord> recordQueue, String topic){
    this.recordQueue = recordQueue;
    this.topic = topic;

    this.keySchema = SchemaBuilder.struct().name("SyslogKey")
        .field(REMOTE_ADDRESS, Schema.STRING_SCHEMA)
        .build();

    this.valueSchema = SchemaBuilder.struct().name("SyslogValue")
        .field(DATE, Timestamp.SCHEMA)
        .field(FACILITY, Schema.INT32_SCHEMA)
        .field(HOST, Schema.STRING_SCHEMA)
        .field(LEVEL, Schema.INT32_SCHEMA)
        .field(MESSAGE, Schema.STRING_SCHEMA)
        .field(CHARSET, Schema.STRING_SCHEMA)
        .field(REMOTE_ADDRESS, Schema.STRING_SCHEMA)
        .build();
  }

  @Override
  public void event(SyslogServerIF syslogServerIF, SocketAddress socketAddress, SyslogServerEventIF event) {

    Map<String, String> partition = Collections.singletonMap(HOST, event.getHost());

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
        ;


    SourceRecord sourceRecord = new SourceRecord(
        partition,
        partition,
        this.topic,
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
    if(log.isErrorEnabled()){
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
