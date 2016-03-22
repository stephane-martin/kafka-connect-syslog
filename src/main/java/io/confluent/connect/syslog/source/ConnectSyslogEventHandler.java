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

  final Schema keySchema;
  final Schema valueSchema;
  final BlockingQueue<SourceRecord> recordQueue;
  final String topic;

  public ConnectSyslogEventHandler(BlockingQueue<SourceRecord> recordQueue, String topic){
    this.recordQueue = recordQueue;
    this.topic = topic;

    this.keySchema = SchemaBuilder.struct().name("SyslogKey")
        .field(HOST, Schema.STRING_SCHEMA)
        .build();

    this.valueSchema = SchemaBuilder.struct().name("SyslogValue")
        .field(DATE, Timestamp.SCHEMA)
        .field(FACILITY, Schema.INT32_SCHEMA)
        .field(HOST, Schema.STRING_SCHEMA)
        .field(LEVEL, Schema.INT32_SCHEMA)
        .field(MESSAGE, Schema.STRING_SCHEMA)
        .build();
  }


  @Override
  public void event(SyslogServerIF syslogServerIF, SocketAddress socketAddress, SyslogServerEventIF event) {

    Map<String, String> partition = Collections.singletonMap(HOST, event.getHost());

    Struct keyStruct = new Struct(this.keySchema)
        .put(HOST, event.getHost());
    
    Struct valueStruct = new Struct(this.valueSchema)
        .put(DATE, event.getDate())
        .put(FACILITY, event.getFacility())
        .put(HOST, event.getHost())
        .put(LEVEL, event.getLevel())
        .put(MESSAGE, event.getMessage())
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

  }

  @Override
  public void initialize(SyslogServerIF syslogServerIF) {

  }

  @Override
  public void destroy(SyslogServerIF syslogServerIF) {

  }
}
