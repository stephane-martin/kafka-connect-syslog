package io.confluent.connect.syslog.source.config;

import org.apache.kafka.common.config.ConfigDef;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;
import org.graylog2.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServer;
import org.graylog2.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServerConfigIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SSLTCPSyslogConfig extends TCPSyslogConfig implements SSLTCPNetSyslogServerConfigIF {
  private static final Logger log = LoggerFactory.getLogger(SSLTCPSyslogConfig.class);

  public static final String KEYSTORE_CONFIG="syslog.keystore";
  public static final String KEYSTORE_DOC="Path to the keystore containing the ssl certificate for this host.";

  public static final String KEYSTORE_PASSWORD_CONFIG="syslog.keystore.password";
  public static final String KEYSTORE_PASSWORD_DOC="Password for the keystore.";

  public static final String TRUSTSTORE_CONFIG="syslog.truststore";
  public static final String TRUSTSTORE_DOC="Path to the truststore containing the ssl certificate for this host.";

  public static final String TRUSTSTORE_PASSWORD_CONFIG="syslog.truststore.password";
  public static final String TRUSTSTORE_PASSWORD_DOC="Password for the truststore.";



  protected static ConfigDef getConfig() {
    return TCPSyslogConfig.getConfig()
        .define(KEYSTORE_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, KEYSTORE_DOC)
        .define(KEYSTORE_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, ConfigDef.Importance.HIGH, KEYSTORE_PASSWORD_DOC)
        .define(TRUSTSTORE_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TRUSTSTORE_DOC)
        .define(TRUSTSTORE_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, ConfigDef.Importance.HIGH, TRUSTSTORE_PASSWORD_DOC)
        ;
  }

  public SSLTCPSyslogConfig(Map<String, String> originals) {
    super(getConfig(), originals);
  }

  @Override
  public String getKeyStore() {
    return this.getString(KEYSTORE_CONFIG);
  }

  @Override
  public void setKeyStore(String s) {
    log.warn("Setting {} to {}. Should not happen", KEYSTORE_CONFIG, s);
    throw new UnsupportedOperationException();
  }

  @Override
  public String getKeyStorePassword() {
    return this.getPassword(KEYSTORE_PASSWORD_CONFIG).toString();
  }

  @Override
  public void setKeyStorePassword(String s) {
    log.warn("Setting {} to {}. Should not happen", KEYSTORE_PASSWORD_CONFIG, s);
    throw new UnsupportedOperationException();
  }

  @Override
  public String getTrustStore() {
    return this.getString(TRUSTSTORE_CONFIG);
  }

  @Override
  public void setTrustStore(String s) {
    log.warn("Setting {} to {}. Should not happen", TRUSTSTORE_CONFIG, s);
    throw new UnsupportedOperationException();
  }

  @Override
  public String getTrustStorePassword() {
    return this.getPassword(TRUSTSTORE_PASSWORD_CONFIG).toString();
  }

  @Override
  public void setTrustStorePassword(String s) {
    log.warn("Setting {} to {}. Should not happen", TRUSTSTORE_PASSWORD_CONFIG, s);
    throw new UnsupportedOperationException();
  }

  @Override
  public Class getSyslogServerClass() {
    return SSLTCPNetSyslogServer.class;
  }
}
