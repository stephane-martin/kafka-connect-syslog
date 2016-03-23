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

import org.graylog2.syslog4j.server.impl.net.udp.UDPNetSyslogServer;

import java.util.Map;


public class UDPSyslogSourceConfig extends BaseSyslogSourceConfig {
  public UDPSyslogSourceConfig(Map<String, String> originals) {
    super(baseConfig(), originals);
  }

  @Override
  public Class getSyslogServerClass() {
    return UDPNetSyslogServer.class;
  }
}
