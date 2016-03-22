package io.confluent.connect.syslog.source.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.graylog2.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;

import java.util.Map;

public abstract class BaseSyslogConfig<T extends AbstractNetSyslogServerConfig> extends AbstractConfig {

  public static final String TOPIC_CONFIG="kafka.topic";
  private static final String TOPIC_DOC="Kafka topic to write syslog data to.";


  public static final String HOST_CONFIG="syslog.host";
  private static final String HOST_DOC="Hostname to listen on.";

  public static final String PORT_CONFIG="syslog.port";
  private static final String PORT_DOC="port to listen on.";

  public static final String STRUCTURED_DATA_CONFIG="syslog.structured.data";
  private static final String STRUCTURED_DATA_DOC="Flag to determine if structured data should be used.";

  public static final String MESSAGE_BUFFER_SIZE_CONFIG="message.buffer.size";
  private static final String MESSAGE_BUFFER_SIZE_DOC="Maximum number of documents to buffer in memory before blocking.";

  public static final String BATCH_SIZE_CONFIG="batch.size";
  private static final String BATCH_SIZE_DOC="Number of documents to batch to kafka in a single request.";

  public BaseSyslogConfig(ConfigDef definition, Map<String, String> originals) {
    super(definition, originals);
  }

  protected static ConfigDef baseConfig() {
    return new ConfigDef()
        .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
        .define(HOST_CONFIG, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, HOST_DOC)
        .define(PORT_CONFIG, ConfigDef.Type.INT, ConfigDef.Importance.HIGH, PORT_DOC)
        .define(MESSAGE_BUFFER_SIZE_CONFIG, ConfigDef.Type.INT, 8192, ConfigDef.Importance.MEDIUM, MESSAGE_BUFFER_SIZE_DOC)
        .define(BATCH_SIZE_CONFIG, ConfigDef.Type.INT, 1024, ConfigDef.Importance.MEDIUM, BATCH_SIZE_DOC)
        .define(STRUCTURED_DATA_CONFIG, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW, STRUCTURED_DATA_DOC)
        ;
  }
  
  public String getHost(){
    return this.getString(HOST_CONFIG);
  }

  public int getPort() {
    return this.getInt(PORT_CONFIG);
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

  public boolean getUseStructuredData() {
    return this.getBoolean(STRUCTURED_DATA_CONFIG);
  }

  protected abstract T createSyslog();

  protected void applySettings(T config) {
    config.setHost(this.getHost());
    config.setPort(this.getPort());
    config.setUseStructuredData(this.getUseStructuredData());
  }

  public final T getSyslogServerConfig() {
    T config = createSyslog();
    applySettings(config);
    return config;
  }

}
