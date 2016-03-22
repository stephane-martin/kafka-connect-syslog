package io.confluent.connect.syslog.source;

import io.confluent.connect.syslog.source.config.BaseSyslogConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public abstract class SyslogSourceTask<T extends BaseSyslogConfig> extends SourceTask{
  BlockingQueue<SourceRecord> recordQueue;
  List<SourceRecord> records;
  ConnectSyslogEventHandler syslogEventHandler;
  SyslogServerIF syslogServer;
  int batchSize;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  abstract T createConfig(Map<String, String> props);

  @Override
  public void start(Map<String, String> props) {
    T config = createConfig(props);

    this.batchSize = config.getBatchSize();
    this.recordQueue = new LinkedBlockingQueue<>(config.getMessageBufferSize());
    this.records = new ArrayList<>(this.batchSize);
    this.syslogEventHandler = new ConnectSyslogEventHandler(this.recordQueue, config.getTopic());
    AbstractNetSyslogServerConfig syslogServerConfig = config.getSyslogServerConfig();
    syslogServerConfig.addEventHandler(this.syslogEventHandler);
    this.syslogServer = SyslogServer.createThreadedInstance("asdfa", syslogServerConfig);
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    this.records.clear();

    if(0 == this.recordQueue.drainTo(this.records, this.batchSize)) {
      Thread.sleep(250);
      return this.records;
    }

    return this.records;
  }

  @Override
  public void stop() {
    this.syslogServer.shutdown();
  }
}

