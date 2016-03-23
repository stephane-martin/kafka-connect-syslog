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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public abstract class SyslogSourceTask<T extends BaseSyslogConfig> extends SourceTask{
  BlockingQueue<SourceRecord> recordQueue;
  List<SourceRecord> records;
  ConnectSyslogEventHandler syslogEventHandler;
  SyslogServerIF syslogServer;
  int batchSize;
  int backoffMS;
  T config;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  abstract T createConfig(Map<String, String> props);

  @Override
  public void start(Map<String, String> props) {
    this.config = createConfig(props);
    this.backoffMS = this.config.getBackoff();
    this.batchSize = this.config.getBatchSize();
    this.recordQueue = new LinkedBlockingQueue<>(config.getMessageBufferSize());
    this.records = new ArrayList<>(this.batchSize);
    this.syslogEventHandler = new ConnectSyslogEventHandler(this.recordQueue, config.getTopic());
    this.config.addEventHandler(this.syslogEventHandler);
    this.syslogServer = SyslogServer.createThreadedInstance("asdfa", config);
  }

  CountDownLatch stopLatch = new CountDownLatch(1);

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    this.records.clear();

    this.recordQueue.drainTo(this.records, this.batchSize);

    if(!this.records.isEmpty()){
      return this.records;
    }

    if(this.stopLatch.await(this.backoffMS, TimeUnit.MILLISECONDS)){

    }
    return this.records;
  }

  @Override
  public void stop() {
    this.stopLatch.countDown();
    this.syslogServer.shutdown();
  }
}

