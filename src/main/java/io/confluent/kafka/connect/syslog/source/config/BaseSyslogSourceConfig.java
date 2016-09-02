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

public abstract class BaseSyslogSourceConfig extends AbstractConfig implements SyslogServerConfigIF {
  public static final String TOPIC_CONFIG = "kafka.topic";
  public static final String HOST_CONFIG = "syslog.host";
  public static final String PORT_CONFIG = "syslog.port";
  public static final String CHARSET_CONFIG = "syslog.charset";
  public static final String SHUTDOWN_WAIT_CONFIG = "syslog.shutdown.wait";
  public static final String STRUCTURED_DATA_CONFIG = "syslog.structured.data";
  public static final String BACKOFF_CONFIG = "backoff.ms";
  public static final String REVERSE_DNS_IP_CONF = "syslog.reverse.dns.remote.ip";
  public static final String REVERSE_DNS_CACHE_TTL_CONF = "syslog.reverse.dns.cache.ms";
  public static final String BATCH_SIZE_CONF = "batch.size";
  private static final Logger log = LoggerFactory.getLogger(BaseSyslogSourceConfig.class);
  private static final String TOPIC_DOC = "Kafka topic to write syslog data to.";
  private static final String HOST_DOC = "Hostname to listen on.";
  private static final String PORT_DOC = "Port to listen on.";
  private static final String CHARSET_DOC = "Character set for syslog messages.";
  private static final String CHARSET_DEFAULT = "UTF-8";
  private static final String SHUTDOWN_WAIT_DOC = "The amount of time in milliseconds to wait for messages when shutting down the server.";
  private static final long SHUTDOWN_WAIT_DEFAULT = SyslogConstants.SERVER_SHUTDOWN_WAIT_DEFAULT;
  private static final String STRUCTURED_DATA_DOC = "Flag to determine if structured data should be used.";
  private static final String BACKOFF_DOC = "Number of milliseconds to sleep when no data is returned.";
  private static final String REVERSE_DNS_IP_DOC = "Flag to determine if the ip address of the remote sender should be resolved. If set to false the hostname value will be null.";
  private static final String REVERSE_DNS_CACHE_TTL_DOC = "The amount of time to cache the reverse lookup values from DNS.";
  private static final String BATCH_SIZE_DOC = "The number of records to pull off of the queue at once.";

  final List<SyslogServerEventHandlerIF> eventhandlers;

  public BaseSyslogSourceConfig(ConfigDef definition, Map<String, String> originals) {
    super(definition, originals);

    this.eventhandlers = new ArrayList<>();
  }

  protected static ConfigDef config() {
    return new ConfigDef()
        .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
        .define(HOST_CONFIG, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, HOST_DOC)
        .define(PORT_CONFIG, ConfigDef.Type.INT, ConfigDef.Importance.HIGH, PORT_DOC)
        .define(STRUCTURED_DATA_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW, STRUCTURED_DATA_DOC)
        .define(BACKOFF_CONFIG, ConfigDef.Type.INT, 100, ConfigDef.Range.atLeast(50), ConfigDef.Importance.LOW, BACKOFF_DOC)
        .define(CHARSET_CONFIG, ConfigDef.Type.STRING, CHARSET_DEFAULT, ConfigDef.Importance.LOW, CHARSET_DOC)
        .define(SHUTDOWN_WAIT_CONFIG, ConfigDef.Type.LONG, SHUTDOWN_WAIT_DEFAULT, ConfigDef.Importance.LOW, SHUTDOWN_WAIT_DOC)
        .define(REVERSE_DNS_IP_CONF, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW, REVERSE_DNS_IP_DOC)
        .define(REVERSE_DNS_CACHE_TTL_CONF, ConfigDef.Type.LONG, 60000, ConfigDef.Importance.LOW, REVERSE_DNS_CACHE_TTL_DOC)
        .define(BATCH_SIZE_CONF, ConfigDef.Type.INT, 5000, ConfigDef.Importance.LOW, BATCH_SIZE_DOC);
  }

  @Override
  public String getHost() {
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

  public String topic() {
    return this.getString(TOPIC_CONFIG);
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

  public boolean reverseDnsIP() {
    return this.getBoolean(REVERSE_DNS_IP_CONF);
  }

  public long reverseDnsCacheTtl() {
    return this.getLong(REVERSE_DNS_CACHE_TTL_CONF);
  }

  public int batchSize() {
    return this.getInt(BATCH_SIZE_CONF);
  }

  public int backoffMS() {
    return this.getInt(BACKOFF_CONFIG);
  }

}
