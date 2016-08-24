/**
 * Copyright (C) 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.confluent.kafka.connect.syslog.source.config;

import org.apache.kafka.common.config.ConfigDef;
import org.graylog2.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServer;
import org.graylog2.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServerConfigIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SSLTCPSyslogSourceConfig extends TCPSyslogSourceConfig implements SSLTCPNetSyslogServerConfigIF {
  public static final String KEYSTORE_CONFIG = "syslog.keystore";
  public static final String KEYSTORE_DOC = "Path to the keystore containing the ssl certificate for this host.";
  public static final String KEYSTORE_PASSWORD_CONFIG = "syslog.keystore.password";
  public static final String KEYSTORE_PASSWORD_DOC = "Password for the keystore.";
  public static final String TRUSTSTORE_CONFIG = "syslog.truststore";
  public static final String TRUSTSTORE_DOC = "Path to the truststore containing the ssl certificate for this host.";
  public static final String TRUSTSTORE_PASSWORD_CONFIG = "syslog.truststore.password";
  public static final String TRUSTSTORE_PASSWORD_DOC = "Password for the truststore.";
  private static final Logger log = LoggerFactory.getLogger(SSLTCPSyslogSourceConfig.class);


  public SSLTCPSyslogSourceConfig(Map<String, String> originals) {
    super(getConfig(), originals);
  }

  public static ConfigDef getConfig() {
    return TCPSyslogSourceConfig.getConfig()
        .define(KEYSTORE_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, KEYSTORE_DOC)
        .define(KEYSTORE_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, ConfigDef.Importance.HIGH, KEYSTORE_PASSWORD_DOC)
        .define(TRUSTSTORE_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TRUSTSTORE_DOC)
        .define(TRUSTSTORE_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, ConfigDef.Importance.HIGH, TRUSTSTORE_PASSWORD_DOC);
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
