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

import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.etourdot.vertx.marklogic.client.MarkLogicClientService;
import org.etourdot.vertx.marklogic.management.MarkLogicManagementService;
import org.etourdot.vertx.marklogic.model.options.HostsOptions;
import org.junit.Test;

public class MarklogicServiceVerticleTest extends VertxTestBase {

  @Test
  public void testDeployClient() throws Exception {
    MarkLogicConfig config = new MarkLogicConfig()
      .host("localhost").port(8003).user("rest-writer").password("x").authentication("digest");
    DeploymentOptions options = new DeploymentOptions().setConfig(config.toJson());
    vertx.deployVerticle("service:org.etourdot.vertx.marklogic-client", options, onSuccess(id -> {
      MarkLogicClientService clientService = MarkLogicClientService.createEventBusProxy(vertx, "document.address");
      assertNotNull(clientService);
      testComplete();
    }));
    await();
  }

  @Test
  public void testDeployBadClientPort() throws Exception {
    MarkLogicConfig config = new MarkLogicConfig()
      .host("localhost").port(8999).user("rest-writer").password("x").authentication("digest");
    DeploymentOptions options = new DeploymentOptions().setConfig(config.toJson());
    vertx.deployVerticle("service:org.etourdot.vertx.marklogic-client", options, onFailure(exception -> testComplete() ));
    await();
  }

  @Test
  public void testDeployBadClientAuth() throws Exception {
    MarkLogicConfig config = new MarkLogicConfig()
      .host("localhost").port(8003).user("bad-user").password("bad-password").authentication("digest");
    DeploymentOptions options = new DeploymentOptions().setConfig(config.toJson());
    vertx.deployVerticle("service:org.etourdot.vertx.marklogic-client", options, onFailure(exception -> testComplete() ));
    await();
  }

  @Test
  public void testDeployManagement() throws Exception {
    MarkLogicConfig config = new MarkLogicConfig()
      .host("localhost").port(8002).user("admin").password("admin").authentication("digest");
    DeploymentOptions options = new DeploymentOptions().setConfig(config.toJson());
    vertx.deployVerticle("service:org.etourdot.vertx.marklogic-management", options, onSuccess(id -> {
      HostsOptions hostsOption = new HostsOptions();
      MarkLogicManagementService managementService = MarkLogicManagementService.createEventBusProxy(vertx, "management.address");
      assertNotNull(managementService);
      managementService.getHosts(hostsOption, r -> {
        System.out.println(r);
      });
      testComplete();
    }));
    await();
  }

}
