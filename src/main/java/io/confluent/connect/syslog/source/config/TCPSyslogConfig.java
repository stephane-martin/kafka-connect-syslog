package io.confluent.connect.syslog.source.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.graylog2.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;

import java.util.Map;


public class TCPSyslogConfig extends BaseSyslogConfig<TCPNetSyslogServerConfig> {

  public static String TIMEOUT_CONFIG = "syslog.timeout";
  private static String TIMEOUT_DOC = "timeout";

  public static String BACKLOG_CONFIG = "syslog.backlog";
  private static String BACKLOG_DOC = "backlog";

  public static String MAX_ACTIVE_SOCKETS_CONFIG = "syslog.max.active.sockets";
  private static String MAX_ACTIVE_SOCKETS_DOC = "Maximum active sockets";

  public static String MAX_ACTIVE_SOCKETS_BEHAVIOR_CONFIG = "syslog.max.active.sockets.behavior";
  private static String MAX_ACTIVE_SOCKETS_BEHAVIOR_DOC = "Maximum active sockets";

  private static ConfigDef getConfig() {
    return baseConfig()
        .define(TIMEOUT_CONFIG, Type.INT, 0, Importance.LOW, TIMEOUT_DOC)
        .define(BACKLOG_CONFIG, Type.INT, 0, Importance.LOW, BACKLOG_DOC)
        .define(MAX_ACTIVE_SOCKETS_CONFIG, Type.INT, TCPNetSyslogServerConfigIF.TCP_MAX_ACTIVE_SOCKETS_DEFAULT, Importance.LOW, MAX_ACTIVE_SOCKETS_DOC)
        .define(MAX_ACTIVE_SOCKETS_BEHAVIOR_CONFIG, Type.INT, (int)TCPNetSyslogServerConfigIF.MAX_ACTIVE_SOCKETS_BEHAVIOR_BLOCK, Importance.LOW, MAX_ACTIVE_SOCKETS_BEHAVIOR_DOC)
        ;
  }

  public TCPSyslogConfig(Map<String, String> originals) {
    super(getConfig(), originals);
  }

  public int getTimeout() {
    return this.getInt(TIMEOUT_CONFIG);
  }

  public int getBacklog() {
    return this.getInt(BACKLOG_CONFIG);
  }

  public int getMaxActiveSockets() {
    return this.getInt(MAX_ACTIVE_SOCKETS_CONFIG);
  }

  public byte getMaxActiveSocketsBehavior() {
    //TODO: Fix this later.
    return TCPNetSyslogServerConfigIF.MAX_ACTIVE_SOCKETS_BEHAVIOR_BLOCK;
  }

  @Override
  protected TCPNetSyslogServerConfig createSyslog() {
    return new TCPNetSyslogServerConfig();
  }

  @Override
  protected void applySettings(TCPNetSyslogServerConfig config) {
    super.applySettings(config);
    config.setBacklog(this.getBacklog());
    config.setMaxActiveSockets(this.getMaxActiveSockets());
    config.setMaxActiveSocketsBehavior(this.getMaxActiveSocketsBehavior());
  }
}
