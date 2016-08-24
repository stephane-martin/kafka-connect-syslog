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
package io.confluent.kafka.connect.syslog.source;

import io.confluent.kafka.connect.syslog.source.config.BaseSyslogSourceConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.SyslogServerIF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;


public abstract class SyslogSourceTask<T extends BaseSyslogSourceConfig> extends SourceTask {
  ConcurrentLinkedDeque<SourceRecord> messageQueue;
  ConnectSyslogEventHandler syslogEventHandler;
  SyslogServerIF syslogServer;
  int backoffMS;
  T config;
  CountDownLatch stopLatch = new CountDownLatch(1);

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  abstract T createConfig(Map<String, String> props);

  @Override
  public void start(Map<String, String> props) {
    this.config = createConfig(props);
    this.messageQueue = new ConcurrentLinkedDeque<>();
    this.syslogEventHandler = new ConnectSyslogEventHandler(this.messageQueue, this.config);
    this.config.addEventHandler(this.syslogEventHandler);
    this.syslogServer = SyslogServer.createThreadedInstance("Syslog Server", config);
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new ArrayList<>(256);

    while (records.isEmpty()) {
      int size = messageQueue.size();

      for (int i = 0; i < size; i++) {
        SourceRecord record = this.messageQueue.poll();

        if (null == record) {
          break;
        }

        records.add(record);
      }

      if (records.isEmpty()) {
        Thread.sleep(100);
      }
    }

    return records;
  }

  @Override
  public void stop() {
    this.stopLatch.countDown();
    this.syslogServer.shutdown();
  }
}

