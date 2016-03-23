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
package io.confluent.connect.syslog.source.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.graylog2.syslog4j.SyslogConstants;
import org.graylog2.syslog4j.SyslogRuntimeException;
import org.graylog2.syslog4j.server.SyslogServerConfigIF;
import org.graylog2.syslog4j.server.SyslogServerEventHandlerIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseSyslogConfig extends AbstractConfig implements SyslogServerConfigIF {
  private static final Logger log = LoggerFactory.getLogger(BaseSyslogConfig.class);

  public static final String TOPIC_CONFIG="kafka.topic";
  private static final String TOPIC_DOC="Kafka topic to write syslog data to.";

  public static final String HOST_CONFIG="syslog.host";
  private static final String HOST_DOC="Hostname to listen on.";

  public static final String PORT_CONFIG="syslog.port";
  private static final String PORT_DOC="Port to listen on.";

  public static final String CHARSET_CONFIG="syslog.charset";
  private static final String CHARSET_DOC="Character set for syslog messages.";
  private static final String CHARSET_DEFAULT="UTF-8";

  public static final String SHUTDOWN_WAIT_CONFIG="syslog.shutdown.wait";
  private static final String SHUTDOWN_WAIT_DOC="The amount of time in milliseconds to wait for messages when shutting down the server.";
  private static final long SHUTDOWN_WAIT_DEFAULT= SyslogConstants.SERVER_SHUTDOWN_WAIT_DEFAULT;

  public static final String STRUCTURED_DATA_CONFIG="syslog.structured.data";
  private static final String STRUCTURED_DATA_DOC="Flag to determine if structured data should be used.";

  public static final String MESSAGE_BUFFER_SIZE_CONFIG="message.buffer.size";
  private static final String MESSAGE_BUFFER_SIZE_DOC="Maximum number of documents to buffer in memory before blocking.";

  public static final String BATCH_SIZE_CONFIG="batch.size";
  private static final String BATCH_SIZE_DOC="Number of documents to batch to kafka in a single request.";

  public static final String BACKOFF_CONFIG="backoff.ms";
  private static final String BACKOFF_DOC="Number of milliseconds to sleep when no data is returned.";

  final List<SyslogServerEventHandlerIF> eventhandlers;

  public BaseSyslogConfig(ConfigDef definition, Map<String, String> originals) {
    super(definition, originals);

    this.eventhandlers = new ArrayList<>();
  }

  protected static ConfigDef baseConfig() {
    return new ConfigDef()
        .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
        .define(HOST_CONFIG, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, HOST_DOC)
        .define(PORT_CONFIG, ConfigDef.Type.INT, ConfigDef.Importance.HIGH, PORT_DOC)
        .define(MESSAGE_BUFFER_SIZE_CONFIG, ConfigDef.Type.INT, 8192, ConfigDef.Importance.MEDIUM, MESSAGE_BUFFER_SIZE_DOC)
        .define(BATCH_SIZE_CONFIG, ConfigDef.Type.INT, 1024, ConfigDef.Importance.MEDIUM, BATCH_SIZE_DOC)
        .define(STRUCTURED_DATA_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW, STRUCTURED_DATA_DOC)
        .define(BACKOFF_CONFIG, ConfigDef.Type.INT, 500, ConfigDef.Range.atLeast(50), ConfigDef.Importance.LOW, BACKOFF_DOC)
        .define(CHARSET_CONFIG, ConfigDef.Type.STRING, CHARSET_DEFAULT, ConfigDef.Importance.LOW, CHARSET_DOC)
        .define(SHUTDOWN_WAIT_CONFIG, ConfigDef.Type.LONG, SHUTDOWN_WAIT_DEFAULT, ConfigDef.Importance.LOW, SHUTDOWN_WAIT_DOC)
        ;
  }



  @Override
  public String getHost(){
    return this.getString(HOST_CONFIG);
  }

  @Override
  public void setHost(String s) throws SyslogRuntimeException {
    log.warn("Setting {} to {}. Should not happen", HOST_CONFIG, s);
    throw new UnsupportedOperationException();
  }

  @Override
  public int getPort() {
    return this.getInt(PORT_CONFIG);
  }

  @Override
  public void setPort(int i) throws SyslogRuntimeException {
    log.warn("Setting {} to {}. Should not happen", PORT_CONFIG, i);
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isUseDaemonThread() {
    return true;
  }

  @Override
  public void setUseDaemonThread(boolean b) {
    log.warn("Setting {} to {}. Should not happen", "daemonthread", b);
    throw new UnsupportedOperationException();
  }

  @Override
  public int getThreadPriority() {
    return SyslogConstants.THREAD_PRIORITY_DEFAULT;
  }

  @Override
  public void setThreadPriority(int i) {
    log.warn("Setting {} to {}. Should not happen", SHUTDOWN_WAIT_CONFIG, i);
    throw new UnsupportedOperationException();
  }

  @Override
  public List getEventHandlers() {
    return this.eventhandlers;
  }

  @Override
  public long getShutdownWait() {
    return this.getLong(SHUTDOWN_WAIT_CONFIG);
  }

  @Override
  public void setShutdownWait(long l) {
    log.warn("Setting {} to {}. Should not happen", SHUTDOWN_WAIT_CONFIG, l);
    throw new UnsupportedOperationException();
  }

  @Override
  public void addEventHandler(SyslogServerEventHandlerIF syslogServerEventHandlerIF) {
    this.eventhandlers.add(syslogServerEventHandlerIF);
  }

  @Override
  public void insertEventHandler(int i, SyslogServerEventHandlerIF syslogServerEventHandlerIF) {
    this.eventhandlers.add(i, syslogServerEventHandlerIF);
  }

  @Override
  public void removeEventHandler(SyslogServerEventHandlerIF syslogServerEventHandlerIF) {
    this.eventhandlers.remove(syslogServerEventHandlerIF);
  }

  @Override
  public void removeAllEventHandlers() {
    this.eventhandlers.clear();
  }

  @Override
  public boolean isUseStructuredData() {
    return this.getBoolean(STRUCTURED_DATA_CONFIG);
  }

  @Override
  public void setUseStructuredData(boolean b) {
    log.warn("Setting {} to {}. Should not happen", STRUCTURED_DATA_CONFIG, b);
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getDateTimeFormatter() {
    return null;
  }

  @Override
  public void setDateTimeFormatter(Object o) {
    log.warn("Setting {} to {}. Should not happen", "dateformatter", o);
    throw new UnsupportedOperationException();
  }

  public String getTopic() {
    return this.getString(TOPIC_CONFIG);
  }

  public int getBatchSize() {
    return this.getInt(BATCH_SIZE_CONFIG);
  }

  public int getMessageBufferSize() {
    return this.getInt(MESSAGE_BUFFER_SIZE_CONFIG);
  }

  public int getBackoff() {
    return this.getInt(BACKOFF_CONFIG);
  }

  @Override
  public String getCharSet() {
    return this.getString(CHARSET_CONFIG);
  }

  @Override
  public void setCharSet(String s) {
    log.warn("Setting {} to {}. Should not happen", CHARSET_CONFIG, s);
    throw new UnsupportedOperationException();
  }
}
