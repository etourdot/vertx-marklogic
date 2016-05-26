/*
 * Copyright (C) 2015 - 2016 Emmanuel Tourdot
 *
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.etourdot.vertx.marklogic;

import org.etourdot.vertx.marklogic.model.management.Host;
import org.etourdot.vertx.marklogic.model.management.HostArray;
import org.etourdot.vertx.marklogic.model.options.HostsOptions;
import org.junit.Test;

public class MarklogicManagementHostTest extends AbstractTestMarklogicDocument {

  @Test
  public void get_host_conf_all() throws Exception {
    HostsOptions hostsOption = new HostsOptions();
    hostsOption.view("status");
    mlManagement.getHosts(hostsOption, onSuccess(s -> {
      assertNotNull(s.getJsonObject("host-status-list"));
      testComplete();
    }));
    await();
  }

  @Test
  public void get_host_conf_one() throws Exception {
    HostsOptions hostsOption = new HostsOptions();

    mlManagement.getHosts(hostsOption, onSuccess(s1 -> {
      HostArray hosts = new HostArray(s1);
      String hostName = hosts.getName(0);
      String id = hosts.getId(0);
      hostsOption.id(id);
      hostsOption.view("status");
      mlManagement.getHosts(hostsOption, onSuccess(s2 -> {
        Host host = new Host().name(s2);
        assertEquals(hostName, host.getName());
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void host_get_properties() throws Exception {
    HostsOptions hostsOption = new HostsOptions();
    mlManagement.getHosts(hostsOption, onSuccess(s1 -> {
      HostArray hosts = new HostArray(s1);
      String hostName = hosts.getName(0);
      hostsOption.name(hostName);
      mlManagement.getHostProps(hostsOption, onSuccess(s2 -> {
        assertEquals("Default", s2.getString("group"));
        testComplete();
      }));
    }));
    await();
  }

}
