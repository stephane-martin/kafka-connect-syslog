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
package com.github.jcustenborder.kafka.connect.syslog;

import com.github.jcustenborder.kafka.connect.syslog.config.BaseSyslogSourceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class HostnameResolverImplTest {
  HostnameResolverImpl resolver;

  BaseSyslogSourceConfig config;

  @BeforeEach
  public void setup() {
    this.config = TCPSyslogConfigTest.config();
    this.resolver = new HostnameResolverImpl(this.config);
  }

  @Test
  public void resolve() {
    final InetSocketAddress input = new InetSocketAddress("8.8.8.8", 53);
    assertFalse(input.isUnresolved(), "IP Address should not have been resolved already");
    InetSocketAddress output = this.resolver.resolve(input);
    assertNotNull(output, "output should not be null.");
    assertFalse(output.isUnresolved());
    assertEquals("google-public-dns-a.google.com", output.getHostName(), "hostname does not match");
    assertEquals(0, this.resolver.reverseDnsCache.stats().hitCount());
    output = this.resolver.resolve(input);
    assertNotNull(output, "output should not be null.");
    assertFalse(output.isUnresolved());
    assertEquals("google-public-dns-a.google.com", output.getHostName(), "hostname does not match");
    System.out.println(this.resolver.reverseDnsCache.stats());
    assertEquals(1, this.resolver.reverseDnsCache.stats().hitCount());
  }
}
