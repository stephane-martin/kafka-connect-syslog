package io.confluent.kafka.connect.syslog.source;

import io.confluent.kafka.connect.syslog.source.config.BaseSyslogSourceConfig;
import io.confluent.kafka.connect.syslog.source.config.TCPSyslogConfigTest;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class HostnameResolverImplTest {
  HostnameResolverImpl resolver;

  BaseSyslogSourceConfig config;

  @Before
  public void setup() {
    this.config = TCPSyslogConfigTest.config();
    this.resolver = new HostnameResolverImpl(this.config);
  }

  @Test
  public void resolve() {
    final InetSocketAddress input = new InetSocketAddress("8.8.8.8", 53);
    assertFalse("IP Address should not have been resolved already", input.isUnresolved());
    InetSocketAddress output = this.resolver.resolve(input);
    assertNotNull("output should not be null.", output);
    assertFalse(output.isUnresolved());
    assertEquals("hostname does not match", "google-public-dns-a.google.com", output.getHostName());
    assertEquals(0, this.resolver.reverseDnsCache.stats().hitCount());
    output = this.resolver.resolve(input);
    assertNotNull("output should not be null.", output);
    assertFalse(output.isUnresolved());
    assertEquals("hostname does not match", "google-public-dns-a.google.com", output.getHostName());
    System.out.println(this.resolver.reverseDnsCache.stats());
    assertEquals(1, this.resolver.reverseDnsCache.stats().hitCount());
  }
}
