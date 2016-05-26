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

package org.etourdot.vertx.marklogic.management;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ProxyHelper;
import org.etourdot.vertx.marklogic.MarkLogicConfig;

public class MarkLogicManagementVerticle extends AbstractVerticle {

  public static final String ML_ADMIN_ADDRESS = "ml.admin.address";
  private Logger logger = LoggerFactory.getLogger(MarkLogicManagementVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    logger.info("Start deploying MarkLogicManagementVerticle");
    MarkLogicConfig config = new MarkLogicConfig(config());
    MarkLogicManagementService managementService = new MarkLogicManagementServiceImpl(MarkLogicManagement.create(vertx, config));
    managementService.availability(result -> {
        if (result.succeeded()) {
          // And register them on the event bus against the configured addresses
          logger.debug("Register clientService " + managementService + " on " + ML_ADMIN_ADDRESS);
          ProxyHelper.registerService(MarkLogicManagementService.class, vertx, managementService, ML_ADMIN_ADDRESS);
          startFuture.complete();
        } else {
          startFuture.fail("Unable to start MarkLogicClientVerticle: " + result.cause().getMessage());
        }
      });
    logger.info("End deploying MarkLogicManagementVerticle");
  }
}
